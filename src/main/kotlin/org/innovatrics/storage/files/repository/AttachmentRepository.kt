package org.innovatrics.storage.files.repository

import org.innovatrics.storage.files.model.Attachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AttachmentRepository:JpaRepository<Attachment,Long> {
    fun findByFileName(fileName: String): Attachment?
    fun findByUserId(userId: UUID): List<Attachment>
}