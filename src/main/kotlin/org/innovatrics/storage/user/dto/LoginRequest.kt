package org.innovatrics.storage.user.dto

data class LoginRequest(
    val username: String,
    val password: String
) {
}