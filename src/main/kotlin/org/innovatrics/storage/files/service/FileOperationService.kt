package org.innovatrics.storage.files.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.minio.http.Method
import jakarta.servlet.http.HttpServletRequest
import org.innovatrics.storage.files.dto.FileOperationResponse
import org.innovatrics.storage.files.dto.PreSignedResponse
import org.innovatrics.storage.minio.dto.EventResponse
import org.innovatrics.storage.minio.dto.MinioPreSignedUrl
import org.innovatrics.storage.minio.service.MinioService
import org.innovatrics.storage.security.service.JwtService
import java.lang.IllegalStateException
import org.slf4j.Logger
import org.slf4j.LoggerFactory


import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


abstract class FileOperationService(
    private val minioService: MinioService,
    private val jwtService: JwtService,
    private val bucketName: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    abstract fun receiveEventUploadFromMinio(message: String)
    abstract fun getAllFiles(): List<FileOperationResponse>
    abstract fun downloadFile(fileName: String): PreSignedResponse

    fun createUploadLink(
        fileName: String,
        httpServletRequest: HttpServletRequest
    ): PreSignedResponse {

        val minioPreSignedUrl = MinioPreSignedUrl(fileName, bucketName, null, Method.PUT)

        httpServletRequest.getHeader("Authorization")?.let { token ->
            minioPreSignedUrl.token = token.substring(7)
        }

        val preSignedUrl = minioService.formingPresignedUrl(minioPreSignedUrl)
        return PreSignedResponse(fileName, preSignedUrl)
    }


    fun receiveEventResponseData(message: String): EventResponse {
        val mapper = jacksonObjectMapper()
        try {
            val rootNode: JsonNode = mapper.readTree(message)
            val encodedFileName = rootNode.at("/Records/0/s3/object/key")?.asText()
            val jwtToken = rootNode.at("/Records/0/s3/object/userMetadata/X-Amz-Meta-Jwt-Token")?.asText()

            if (encodedFileName != null && jwtToken != null) {
                return getMetaData(encodedFileName, jwtToken)
            }

        } catch (e: Exception) {
            logger.error("Error during processing message: ${e.message}")
        }
        throw IllegalStateException("Invalid data format")

    }


    private fun getMetaData(encodedFileName: String, jwtToken: String): EventResponse {
        val decodedFileName = URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString())
        val dateTimeString = decodedFileName.split("_").getOrNull(1)

        dateTimeString?.let { dateUpload ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val localDateTime = LocalDateTime.parse(dateUpload, formatter)

            jwtService.parseJwtToken(jwtToken)?.let { userId ->
                return EventResponse(
                    fileName = decodedFileName,
                    createdBy = UUID.fromString(userId.subject.toString()),
                    createdAt = localDateTime
                )
            }
        }
        throw IllegalStateException("Invalid meta data")
    }


    fun getPreSignedDownloadUrl(fileName: String): PreSignedResponse {
        val preSignedUrl = minioService
            .getDowloadsPresignedUrl(bucketName, fileName)
        return PreSignedResponse(fileName, preSignedUrl)
    }

}