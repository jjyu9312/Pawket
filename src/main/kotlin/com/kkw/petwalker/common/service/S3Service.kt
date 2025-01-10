package com.kkw.petwalker.common.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Paths

@Service
class S3Service(private val s3Client: S3Client) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun uploadFile(bucketName: String, filePath: String, key: String): String {
        try {
            val file = Paths.get(filePath).toFile()

            logger.info("Uploading file: $filePath to S3 bucket: $bucketName with key: $key")

            if (!file.exists()) {
                logger.error("File not found: $filePath")
                throw IllegalArgumentException("File not found: $filePath")
            }

            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()

            s3Client.putObject(putObjectRequest, file.toPath())

            return "https://${bucketName}.s3.amazonaws.com/${key}"

        } catch (e: Exception) {
            logger.error("Failed to upload file to S3: $e")
            throw e
        }
    }
}