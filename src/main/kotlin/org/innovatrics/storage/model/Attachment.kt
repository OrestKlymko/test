package org.innovatrics.storage.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "attachment")
data class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    val id:  UUID,
    @Column(name = "FILE_NAME", nullable = false)
    val fileName: String,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime
)
