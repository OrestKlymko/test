package org.innovatrics.storage.files.repository

import org.innovatrics.storage.files.model.Face
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FaceRepository:JpaRepository<Face, UUID> {
    fun findFacesByUserId(userId: UUID): List<Face>
    fun findByFileName(fileName: String): Face?
}