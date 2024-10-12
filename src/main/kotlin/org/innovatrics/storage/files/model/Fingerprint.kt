package org.innovatrics.storage.files.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
@Table(name = "FINGERPRINT")
@Entity
data class Fingerprint(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    private val id: UUID? = null,
    @Column(name = "FILE_NAME", nullable = false)
    val fileName: String,
    @Column(name = "CREATED_AT", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "CREATED_BY", nullable = false)
    val userId: UUID,
    @Column(name = "FINGERPRINT_POSITION", nullable = true)
    val positionFinger:Long? = null
) {
}