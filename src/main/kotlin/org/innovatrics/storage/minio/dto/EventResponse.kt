package org.innovatrics.storage.minio.dto

import java.time.LocalDateTime
import java.util.*

data class EventResponse(
    val fileName: String,
    val createdBy: UUID,
    val createdAt:LocalDateTime
) {
}