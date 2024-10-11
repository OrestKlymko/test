package org.innovatrics.storage.repository

import org.innovatrics.storage.model.Attachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AttachmentRepository:JpaRepository<Attachment,Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Attachment a WHERE a.fileName = :fileName")
    fun existsByFileName(fileName: String): Boolean
}