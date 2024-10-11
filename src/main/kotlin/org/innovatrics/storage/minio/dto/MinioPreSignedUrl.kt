package org.innovatrics.storage.minio.dto

import io.minio.http.Method

data class MinioPreSignedUrl(
    val fileName: String,
    val bucketName: String,
    var token: String? = null,
    val method:Method){

}
