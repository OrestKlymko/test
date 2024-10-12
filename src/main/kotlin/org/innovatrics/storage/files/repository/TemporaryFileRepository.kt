package org.innovatrics.storage.files.repository

import org.innovatrics.storage.files.model.TemporaryFile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TemporaryFileRepository:JpaRepository<TemporaryFile,UUID> {
    fun findByFileName(fileName:String):TemporaryFile?
    fun findByUserId(userId:UUID):List<TemporaryFile>
}