package org.innovatrics.storage.minio.service

import io.minio.BucketExistsArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import jakarta.annotation.PostConstruct
import org.innovatrics.storage.minio.dto.MinioPreSignedUrl
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class MinioService(
    private val minioClient: MinioClient,
) {

    @Value("\${minio.expiry}")
    private var expiry: Int = 600

    @PostConstruct
    private fun createBucketIfNotExists() {
        val buckets = listOf("attachments", "fingerprints", "faces", "videos")
        buckets.forEach {
            val found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(it).build())
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(it).build())
            }
        }

    }

    fun formingPresignedUrl(minioPreSignedUrl: MinioPreSignedUrl): String {
        val objectName = "${UUID.randomUUID()}_${LocalDateTime.now()}_${minioPreSignedUrl.fileName}"

        val extraHeaders = mutableMapOf<String, String>()
        minioPreSignedUrl.token?.let {
            extraHeaders["X-Amz-Meta-Jwt-Token"] = it
        }

        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(minioPreSignedUrl.method)
                .bucket(minioPreSignedUrl.bucketName)
                .`object`(objectName)
                .extraQueryParams(extraHeaders)
                .expiry(expiry)
                .build()
        )
    }

}