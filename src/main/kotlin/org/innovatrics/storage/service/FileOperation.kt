package org.innovatrics.storage

interface FileOperation<T> {
    fun initiatedUpload(fileName: String): UploadResponse
    fun uploadFile(file: T): String
    fun downloadFile(fileName: String): T
}