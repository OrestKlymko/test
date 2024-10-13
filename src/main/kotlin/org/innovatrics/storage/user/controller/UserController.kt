package org.innovatrics.storage.user.controller


import org.innovatrics.storage.user.dto.LoginRequest
import org.innovatrics.storage.user.dto.RegistrationRequest
import org.innovatrics.storage.user.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) : UserControllerApi {

    override fun login(loginRequest: LoginRequest): ResponseEntity<String> =
        ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
            .body(userService.login(loginRequest))

    override fun register(registrationRequest: RegistrationRequest): ResponseEntity<String> =
        ResponseEntity.status(201).body(userService.register(registrationRequest))
}