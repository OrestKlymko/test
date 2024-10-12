package org.innovatrics.storage.user.service

import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.security.service.JwtService
import org.innovatrics.storage.user.model.User
import org.innovatrics.storage.user.repository.UserRepository
import org.innovatrics.storage.user.dto.LoginRequest
import org.innovatrics.storage.user.dto.RegistrationRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {


    fun login(loginRequest: LoginRequest): String {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw AuthenticationException("Invalid username")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw AuthenticationException("Invalid password")
        }

        return jwtService.createJwtToken(user.id.toString())
    }


    fun register(request: RegistrationRequest): String {
        if (userRepository.findByUsername(request.username) != null) {
            throw AuthenticationException("User with username ${request.username} already exists")
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password)
        )

        val savedUser = userRepository.save(user)
        return jwtService.createJwtToken(savedUser.id.toString())
    }

}