package org.innovatrics.storage

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.stereotype.Component
import java.io.File

@Component("attachments")
class Attachment(
    private val minioClient: MinioClient,
) : FileOperation<File> {

    private val bucketName = "attachments"
    private var expiry: Int = 600;

    override fun uploadFile(file: File): String {
        TODO()
    }

    override fun initiatedUpload(fileName: String): UploadResponse {
        val presignedUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.POST)
                .bucket(bucketName)
                .`object`(fileName)
                .expiry(expiry)
                .build()
        )

        return UploadResponse(fileName, presignedUrl)
    }

    override fun downloadFile(fileName: String): File {
        TODO()
    }
}