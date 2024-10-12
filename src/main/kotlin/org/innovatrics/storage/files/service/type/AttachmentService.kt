package org.innovatrics.storage.files.service.type


import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.innovatrics.storage.files.model.Attachment
import org.innovatrics.storage.files.repository.AttachmentRepository
import org.innovatrics.storage.files.service.FileOperationService
import org.innovatrics.storage.security.service.JwtService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service("attachments")
class AttachmentService(
    private val attachmentRepository: AttachmentRepository,
    private val jwtService: JwtService,
    minioService: MinioService,
) : FileOperationService(minioService, jwtService, "attachments") {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    override fun downloadFile(fileName: String): PreSignedResponse {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        attachmentRepository.findByFileName(fileName)?.let {
            if (it.userId != UUID.fromString(userId.toString())) {
                throw AuthenticationException("User is not authorized for download file $fileName")
            }
        } ?: throw FileNotFoundException("File not found")

        return getPreSignedDownloadUrl(fileName)
    }

    override fun getAllFiles(): List<FileOperationResponse> {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        return attachmentRepository.findByUserId(UUID.fromString(userId.toString()))
            .map { FileOperationResponse(it.fileName, "attachments", it.createdAt) }
    }


    @RabbitListener(queues = ["attachments"])
    override fun receiveEventUploadFromMinio(message: String) {
        try {
            val eventResponse = receiveEventResponseData(message)
            attachmentRepository.findByFileName(eventResponse.fileName)?.let {
                logger.error("File with name ${eventResponse.fileName} already exists")
                return;
            }
            Attachment(
                fileName = eventResponse.fileName,
                createdAt = eventResponse.createdAt,
                userId = eventResponse.createdBy
            ).let { attachmentRepository.save(it) }
        } catch (e: Exception) {
            logger.error("Error during processing message from Rabbit in queue attachment: ${e.message}")
        }
    }
}


