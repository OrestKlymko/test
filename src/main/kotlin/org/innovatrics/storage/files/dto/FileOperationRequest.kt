package org.innovatrics.storage.files.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class FileOperationRequest(

    @Schema(description = "Unique file name for the operation", example = "myfile", required = true)
    @NotBlank(message = "File name cannot be empty")
    val fileName: String,

    @Schema(
        description = "Type of the file. Allowed values are 'attachments', 'faces', 'fingerprints', 'videos','temp'",
        allowableValues = ["attachments", "faces", "fingerprints", "videos", "temp"], required = true
    )
    @NotBlank(message = "File type cannot be empty")
    @Pattern(
        regexp = "attachments|faces|fingerprints|videos|temp",
        message = "File type must be one of: attachments, faces, fingerprints, videos,temp"
    )
    val fileType: String
)
