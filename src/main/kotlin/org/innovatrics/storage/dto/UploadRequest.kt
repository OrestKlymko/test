package org.innovatrics.storage

data class UploadRequest(
    val fileName: String,
    val fileType: String
) {
}