package org.innovatrics.storage.exception

import okio.FileNotFoundException
import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.IllegalStateException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalStateException::class,
        FileNotFoundException::class,
        AuthenticationException::class
    )
    fun handleIllegalArgumentException(e: Exception): ResponseEntity<*> =
        ResponseEntity.badRequest().body(e.message)
}
