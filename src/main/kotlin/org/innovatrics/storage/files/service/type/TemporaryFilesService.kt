package org.innovatrics.storage.files.service.type


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.innovatrics.storage.files.model.TemporaryFile
import org.innovatrics.storage.files.repository.TemporaryFileRepository
import org.innovatrics.storage.files.service.FileOperationService
import org.innovatrics.storage.security.service.JwtService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Service("temp")
class TemporaryFilesService(
    private val temporaryFileRepository: TemporaryFileRepository,
    jwtService: JwtService,
    minioService: MinioService,
    private val mapper: ObjectMapper
) : FileOperationService(minioService, jwtService, "temp") {


    @Value("\${minio.tempBucketTimeDays}")
    private var tempBucketTime: Int = 7
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    override fun downloadFile(fileName: String): PreSignedResponse {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        temporaryFileRepository.findByFileName(fileName)?.let {
            if (it.userId != UUID.fromString(userId.toString())) {
                throw AuthenticationException("User is not authorized for download file $fileName")
            }
        } ?: throw FileNotFoundException("File not found")

        return getPreSignedDownloadUrl(fileName)
    }

    override fun getAllFiles(): List<FileOperationResponse> {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        return temporaryFileRepository.findByUserId(UUID.fromString(userId.toString()))
            .map { FileOperationResponse(it.fileName, "temp", it.createdAt, it.expirationDate) }
    }


    @RabbitListener(queues = ["temp"])
    override fun receiveEventUploadFromMinio(message: String) {
        try {
            val eventResponse = receiveEventResponseData(message)
            temporaryFileRepository.findByFileName(eventResponse.fileName)?.let {
                logger.error("File with name $it already exists")
                return;
            }
            TemporaryFile(
                fileName = eventResponse.fileName,
                createdAt = eventResponse.createdAt,
                userId = eventResponse.createdBy,
                expirationDate = LocalDateTime.now().plusDays(tempBucketTime.toLong())
            ).let { temporaryFileRepository.save(it) }
        } catch (e: Exception) {
            logger.error("Error during processing message from Rabbit in queue temp on save: ${e.message}")
        }
    }

    @RabbitListener(queues = ["temp-delete"])
    fun deleteTemporaryFile(message: String) {

        try {
            val rootNode: JsonNode = mapper.readTree(message)
            rootNode.at("/Records/0/s3/object/key")?.asText().let { filename ->
                val decodedFileName = URLDecoder.decode(filename, StandardCharsets.UTF_8.toString())
                temporaryFileRepository.findByFileName(decodedFileName)?.let {
                    temporaryFileRepository.deleteById(it.id()!!)
                }
            }

        } catch (e: Exception) {
            logger.error("Error during processing message from Rabbit in queue temp-delete: ${e.message}")
        }
    }
}


