package org.innovatrics.storage.files.repository

import org.innovatrics.storage.files.model.Fingerprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FingerprintRepository:JpaRepository<Fingerprint,UUID> {
    fun findByUserId(userId: UUID): List<Fingerprint>
    fun findByFileName(fileName: String): Fingerprint?
}