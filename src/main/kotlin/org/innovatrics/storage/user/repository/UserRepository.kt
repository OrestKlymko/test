package org.innovatrics.storage.user.repository

import org.innovatrics.storage.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository:JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}