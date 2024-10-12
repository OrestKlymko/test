package org.innovatrics.storage.files.dto

import java.time.LocalDateTime

data class FileOperationResponse(
    val fileName: String,
    val fileType: String,
    val createdAt:LocalDateTime,
    val expirationDate:LocalDateTime? = null
)
