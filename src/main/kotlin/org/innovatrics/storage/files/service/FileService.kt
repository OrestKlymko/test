package org.innovatrics.storage.files.service

import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.files.dto.FileOperationRequest
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.springframework.stereotype.Service

@Service
class FileService(
    private val fileServiceMap: Map<String, FileOperationService>
) {

    fun createPreSignedUploadLink(request: FileOperationRequest, httpServletRequest: HttpServletRequest): PreSignedResponse {
        val fileOperation = getOperationType(request.fileType)
        return fileOperation.createUploadLink(request.fileName, httpServletRequest)
    }

    fun createPreSignedDownloadLink(fileRequest: FileOperationRequest): PreSignedResponse {
        val fileOperation = getOperationType(fileRequest.fileType)
        return fileOperation.downloadFile(fileRequest.fileName)
    }

    fun getAllFiles(type: String): List<FileOperationResponse> {
        val fileOperation = getOperationType(type)
        return fileOperation.getAllFiles()
    }


    private fun getOperationType(type: String): FileOperationService {
        return fileServiceMap[type]
            ?: throw IllegalArgumentException("Unsupported file type: $type. You can use one of these: ${fileServiceMap.keys}")
    }
}
