package org.innovatrics.storage.service

import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.dto.UploadRequest
import org.innovatrics.storage.dto.UploadResponse

interface FileOperation<T> {
    fun getUploadLink(fileName: UploadRequest, httpServletRequest: HttpServletRequest): UploadResponse
    fun uploadFile(fileName: String): UploadResponse
    fun downloadFile(fileName: UploadRequest):UploadResponse
}