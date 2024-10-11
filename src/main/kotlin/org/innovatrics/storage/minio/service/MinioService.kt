package org.innovatrics.storage.minio.service

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.innovatrics.storage.minio.dto.MinioPreSignedUrl
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class MinioService(
    private val minioClient: MinioClient,

    ) {
    private val expiry = 600 // 10 minutes


    fun formingPresignedUrl(minioPreSignedUrl: MinioPreSignedUrl): String {
        // Генеруємо унікальне ім'я файлу
        val objectName = "${UUID.randomUUID()}_${LocalDateTime.now()}_${minioPreSignedUrl.fileName}"

        // Створюємо мапу для додаткових заголовків
        val extraHeaders = mutableMapOf<String, String>()

        // Додаємо токен до заголовків, якщо він існує
        minioPreSignedUrl.token?.let {
            extraHeaders["X-Amz-Token"] = it
            println(it)
        }


        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(minioPreSignedUrl.method)
                .bucket(minioPreSignedUrl.bucketName)
                .`object`(objectName)
                .extraQueryParams(extraHeaders)  // Включаємо заголовки в підписаний запит
                .expiry(expiry)
                .build()
        )
    }


    fun saveObject(bucketName: String, objectName: String, data: ByteArray) {
        val inputStream = data.inputStream()
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(inputStream, data.size.toLong(), -1)
                .build()
        )
    }
}