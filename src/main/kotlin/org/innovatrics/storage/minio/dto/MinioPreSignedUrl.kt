package org.innovatrics.storage.configuration

import io.minio.http.Method

data class MinioPreSignedUrl(
    val fileName: String,
    val bucketName: String,
    val method:Method,
    val expiry: Int
)
