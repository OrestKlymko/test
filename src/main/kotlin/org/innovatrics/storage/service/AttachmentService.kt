package org.innovatrics.storage.service


import io.minio.http.Method
import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.minio.dto.MinioPreSignedUrl
import org.innovatrics.storage.dto.UploadRequest
import org.innovatrics.storage.dto.UploadResponse
import org.innovatrics.storage.repository.AttachmentRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.io.File


@Service("attachments")
class AttachmentService(
    private val attachmentRepository: AttachmentRepository,
    private val minioService: MinioService
) : FileOperation<File> {

    private val bucketName = "attachments"

    override fun uploadFile(file: String): UploadResponse {
        TODO()
    }

    override fun getUploadLink(
        uploadRequest: UploadRequest,
        httpServletRequest: HttpServletRequest
    ): UploadResponse {
        val minioPreSignedUrl = MinioPreSignedUrl(uploadRequest.fileName, bucketName, null, Method.PUT)

        // Перевіряємо, чи заголовок "token" існує
        httpServletRequest.getHeader("token")?.let { token ->
            minioPreSignedUrl.token = token // Призначаємо значення токена
        }
        println(minioPreSignedUrl.token)
        // Генеруємо pre-signed URL
        val preSignedUrl = minioService.formingPresignedUrl(minioPreSignedUrl)

        return UploadResponse(uploadRequest.fileName, preSignedUrl)
    }

//    @RabbitListener(queues = ["attachments"])
//    fun receiveMessage(message: String) {
//        println("Отримано подію для fingerprints: $message")
//        // Логіка обробки події для fingerprints
//    }
    override fun downloadFile(uploadReqyest: UploadRequest): UploadResponse {
//        val preSignedUrl = minioService
//            .formingPresignedUrl(MinioPreSignedUrl(downloadRequest.fileName, bucketName, Method.GET))
//
//
//        return UploadResponse(downloadRequest.fileName, preSignedUrl);
        TODO()
    }


}