package org.innovatrics.storage.service

import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.dto.UploadRequest
import org.springframework.stereotype.Service

@Service
class FileService(
    private val fileOperationMap: Map<String, FileOperation<*>>
) {


    fun getUploadLink(request: UploadRequest,httpServletRequest: HttpServletRequest): Any {
        val fileOperation = fileOperationMap[request.fileType]
            ?: throw IllegalArgumentException("Unsupported file type: ${request.fileType}")
        return fileOperation.getUploadLink(request,httpServletRequest)
    }

    fun downloadFile(request: UploadRequest): Any {
        val fileOperation = fileOperationMap[request.fileType]
            ?: throw IllegalArgumentException("Unsupported file type: ${request.fileType}")
        return fileOperation.downloadFile(request)
    }


}