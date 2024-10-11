package org.innovatrics.storage

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import org.innovatrics.storage.configuration.MinioPreSignedUrl
import org.springframework.stereotype.Service


@Service
class MinioService(
    private val minioClient: MinioClient,
) {

    fun formingPresignedUrl(minioPreSignedUrl: MinioPreSignedUrl): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(minioPreSignedUrl.method)
                .bucket(minioPreSignedUrl.bucketName)
                .`object`(minioPreSignedUrl.fileName)
                .expiry(minioPreSignedUrl.expiry)
                .build()
        )
    }
}