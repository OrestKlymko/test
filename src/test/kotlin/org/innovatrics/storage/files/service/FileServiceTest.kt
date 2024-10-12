package org.innovatrics.storage.files.service

import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.files.dto.FileOperationRequest
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class FileServiceTest {

    private lateinit var fileService: FileService
    private lateinit var fileOperationService: FileOperationService

    @BeforeEach
    fun setUp() {
        fileOperationService = mock(FileOperationService::class.java)

        val fileServiceMap = mapOf(
            "attachments" to fileOperationService,
            "faces" to fileOperationService
        )

        fileService = FileService(fileServiceMap)
    }

    @Test
    fun `should create pre-signed upload link`() {
        val request = FileOperationRequest("file1", "attachments")
        val httpServletRequest = mock(HttpServletRequest::class.java)
        val preSignedResponse = PreSignedResponse("file1", "http://example.com/upload-link")

        `when`(fileOperationService.createUploadLink("file1", httpServletRequest))
            .thenReturn(preSignedResponse)

        val result = fileService.createPreSignedUploadLink(request, httpServletRequest)

        assertEquals(preSignedResponse, result)
        verify(fileOperationService).createUploadLink("file1", httpServletRequest)
    }



    @Test
    fun `should create pre-signed download link`() {
        val request = FileOperationRequest("file1", "faces")
        val preSignedResponse = PreSignedResponse("file1", "http://example.com/download-link")


        `when`(fileOperationService.downloadFile("file1"))
            .thenReturn(preSignedResponse)


        val result = fileService.createPreSignedDownloadLink(request)
        assertEquals(preSignedResponse, result)
        verify(fileOperationService).downloadFile("file1")
    }


    @Test
    fun `should get all files`() {
        val fileResponse = listOf(FileOperationResponse("file1", "attachments", LocalDateTime.now()), FileOperationResponse("file2", "attachments", LocalDateTime.now()))

        `when`(fileOperationService.getAllFiles()).thenReturn(fileResponse)

        val result = fileService.getAllFiles("attachments")

        assertEquals(fileResponse, result)
        verify(fileOperationService).getAllFiles()
    }

    @Test
    fun `should throw exception for unsupported file type`() {
        val request = FileOperationRequest("file1", "unsupported")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            fileService.createPreSignedUploadLink(request, mock(HttpServletRequest::class.java))
        }

        assertEquals(
            "Unsupported file type: unsupported. You can use one of these: [attachments, faces]",
            exception.message
        )
    }
}
