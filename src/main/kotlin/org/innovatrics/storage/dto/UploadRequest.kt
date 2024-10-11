package org.innovatrics.storage.dto

data class UploadRequest(
    val fileName: String,
    val fileType: String
) {
}