package org.innovatrics.storage.files.service.type

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.innovatrics.storage.files.model.Video
import org.innovatrics.storage.files.repository.VideoRepository
import org.innovatrics.storage.files.service.FileOperationService
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.security.service.JwtService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.util.*


@Service("videos")
class VideoService(
    private val videoRepository: VideoRepository,
    private val jwtService: JwtService,
    minioService: MinioService


) : FileOperationService(minioService, jwtService, "videos") {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun downloadFile(fileName: String): PreSignedResponse {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        videoRepository.findByFileName(fileName)?.let {
            if (it.userId != UUID.fromString(userId.toString())) {
                throw AuthenticationException("User is not authorized for download file $fileName")
            }
        } ?: throw FileNotFoundException("File not found")

        return getPreSignedDownloadUrl(fileName)
    }

    override fun getAllFiles(): List<FileOperationResponse> {
        val userId = SecurityContextHolder.getContext().authentication.principal
            ?: throw AuthenticationException("User not authenticated")

        return videoRepository.findVideosByUserId(UUID.fromString(userId.toString()))
            .map { FileOperationResponse(it.fileName, "videos", it.createdAt) }
    }

    @RabbitListener(queues = ["videos"])
    override fun receiveEventUploadFromMinio(message: String) {
        try {
            val eventResponse = receiveEventResponseData(message)
            videoRepository.findByFileName(eventResponse.fileName)?.let {
                logger.error("File with name ${eventResponse.fileName} already exists")
                return;
            }

            Video(
                fileName = eventResponse.fileName,
                createdAt = eventResponse.createdAt,
                userId = eventResponse.createdBy
            ).let { videoRepository.save(it) }
        } catch (e: Exception) {
            logger.error("Error during processing message from Rabbit in queue in videos: ${e.message}")
        }
    }
}