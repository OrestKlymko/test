package org.innovatrics.storage.files.controller


import org.innovatrics.storage.files.dto.FileOperationRequest
import org.innovatrics.storage.files.service.FileService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileService: FileService
) : FileControllerApi {


    override fun createUploadLink(
        fileOperationRequest: FileOperationRequest,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<*> =
        ResponseEntity.ok(fileService.createPreSignedUploadLink(fileOperationRequest, httpServletRequest))

    override fun createDownloadLink(fileOperationRequest: FileOperationRequest): ResponseEntity<*> =
        ResponseEntity.ok(fileService.createPreSignedDownloadLink(fileOperationRequest))

    override fun getAllFiles(fileType: String): ResponseEntity<*> =
        ResponseEntity.ok(fileService.getAllFiles(fileType))


}
