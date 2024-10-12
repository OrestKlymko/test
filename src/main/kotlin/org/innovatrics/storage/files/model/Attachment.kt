package org.innovatrics.storage.files.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "ATTACHMENT")
data class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    private val id:  UUID?=null,
    @Column(name = "FILE_NAME", nullable = false)
    val fileName: String,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "CREATED_BY", nullable = false)
    val userId: UUID
)
