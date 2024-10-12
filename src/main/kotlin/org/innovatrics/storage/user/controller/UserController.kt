package org.innovatrics.storage.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.user.dto.LoginRequest
import org.innovatrics.storage.user.dto.RegistrationRequest
import org.innovatrics.storage.user.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "Endpoints for user management")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully authenticated",
                content = [Content(mediaType = "text/plain", schema = Schema(implementation = String::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid username or password",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest) = ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
        .body(userService.login(loginRequest))


    @Operation(summary = "User registration", description = "Registers a new user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Successfully registered",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid registration data",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/register")
    fun register(@RequestBody registrationRequest: RegistrationRequest) =
        ResponseEntity.status(201).body(userService.register(registrationRequest))


    @ExceptionHandler(AuthenticationException::class)
    fun handleException(e: AuthenticationException) = ResponseEntity.badRequest().body(e.message)

}
