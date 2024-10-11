package org.innovatrics.storage.controller

import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.dto.UploadRequest
import org.innovatrics.storage.service.FileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileService: FileService
) {
    @GetMapping("/upload")
    fun getAUploadLink(@RequestBody uploadRequest: UploadRequest,httpServletRequest: HttpServletRequest):ResponseEntity<*> {
        return ResponseEntity.ok(fileService.getUploadLink(uploadRequest,httpServletRequest))
    }



    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<*> {
        return ResponseEntity.badRequest().body(e.message)
    }
}