package org.innovatrics.storage.minio.service

import io.minio.*
import io.minio.errors.MinioException
import io.minio.messages.*
import jakarta.annotation.PostConstruct
import org.innovatrics.storage.minio.dto.MinioPreSignedUrl
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


@Service
class MinioService(
    private val minioClient: MinioClient,
) {

    @Value("\${minio.expiry}")
    private var expiry: Int = 600

    @Value("\${minio.tempBucketTimeDays}")
    private var tempBucketTime: Int = 7

    @PostConstruct
    private fun createBucketIfNotExists() {
        val buckets = listOf("attachments", "fingerprints", "faces", "videos")
        buckets.forEach {
            val found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(it).build())
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(it).build())
            }
        }
        setBucketLifecycle(minioClient, "temp")

    }




    fun setBucketLifecycle(minioClient: MinioClient, bucketName: String) {
        try {

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
            }


            val lifecycleRule = LifecycleRule(
                Status.ENABLED,
                null,
                Expiration(null as ZonedDateTime?, tempBucketTime, null),
                RuleFilter(null,"",null),
                null,
                null,
                null,
                null
            )

            val lifecycleConfig = LifecycleConfiguration(listOf(lifecycleRule))
            val lifecycleArgs = SetBucketLifecycleArgs.builder()
                .bucket(bucketName)
                .config(lifecycleConfig)
                .build()

            minioClient.setBucketLifecycle(lifecycleArgs)
        } catch (e: MinioException) {
            throw RuntimeException(e)
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