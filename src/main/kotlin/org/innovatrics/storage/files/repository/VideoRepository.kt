package org.innovatrics.storage.files.repository

import org.innovatrics.storage.files.model.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VideoRepository:JpaRepository<Video, UUID> {
    fun findVideosByUserId(userId: UUID): List<Video>
    fun findByFileName(fileName: String): Video?
}