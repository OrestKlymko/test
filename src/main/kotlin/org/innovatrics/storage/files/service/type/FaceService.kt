package org.innovatrics.storage.files.service.type

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.innovatrics.storage.files.model.Face
import org.innovatrics.storage.files.repository.FaceRepository
import org.innovatrics.storage.files.service.FileOperationService
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.security.service.JwtService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.util.*

@Service("faces")
class FaceService(
    private val faceRepository: FaceRepository,
    jwtService: JwtService,
    minioService: MinioService
) : FileOperationService(minioService, jwtService,"faces") {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    override fun getAllFiles(): List<FileOperationResponse> {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        return faceRepository.findFacesByUserId(UUID.fromString(userId.toString()))
            .map { FileOperationResponse(it.fileName, "faces", it.createdAt) }
    }

    override fun downloadFile(fileName: String): PreSignedResponse {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        faceRepository.findByFileName(fileName)?.let {
            if (it.userId != UUID.fromString(userId.toString())) {
                throw AuthenticationException("User is not authorized for download file $fileName")
            }
        } ?: throw FileNotFoundException("File not found")

        return getPreSignedDownloadUrl(fileName)
    }


    @RabbitListener(queues = ["faces"])
    override fun receiveEventUploadFromMinio(message: String) {
       try {
           val eventResponse = receiveEventResponseData(message)

           faceRepository.findByFileName(eventResponse.fileName)?.let {
               logger.error("File with name ${eventResponse.fileName} already exists")
               return;
           }
           Face(
               fileName = eventResponse.fileName,
               createdAt = eventResponse.createdAt,
               userId = eventResponse.createdBy
           ).let { faceRepository.save(it) }
       }catch (e: Exception){
           logger.error("Error during processing message from Rabbit in queue faces: ${e.message}")
       }
    }


}