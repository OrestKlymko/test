package org.innovatrics.storage.files.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.files.dto.FileOperationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "File Controller", description = "Endpoints for file management")
interface FileControllerApi {

    @Operation(summary = "Create a pre-signed upload link")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created upload link",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "403", description = "Invalid token", content = [Content(mediaType = "application/json")])
    ])
    @PostMapping("/upload")
    fun createUploadLink(
        @RequestBody fileOperationRequest: FileOperationRequest,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<*>

    @Operation(summary = "Create a pre-signed download link")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created download link",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "403", description = "Invalid token", content = [Content(mediaType = "application/json")])
    ])
    @PostMapping("/download")
    fun createDownloadLink(@RequestBody fileOperationRequest: FileOperationRequest): ResponseEntity<*>

    @Operation(summary = "Get all files by type")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of files retrieved",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = List::class))]),
        ApiResponse(responseCode = "400", description = "Invalid file type", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "403", description = "Invalid token", content = [Content(mediaType = "application/json")])
    ])
    @GetMapping("/{fileType}")
    fun getAllFiles(
        @PathVariable
        @Schema(description = "Type of file", allowableValues = ["attachments", "faces", "fingerprints", "videos", "temp"])
        fileType: String
    ): ResponseEntity<*>
}
