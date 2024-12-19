package com.kkw.petwalker.common.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Paths

@Service
class S3Service(private val s3Client: S3Client) {

    fun uploadFile(bucketName: String, filePath: String, key: String): String {
        val file = Paths.get(filePath).toFile()

        if (!file.exists()) {
            throw IllegalArgumentException("File not found: $filePath")
        }

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        s3Client.putObject(putObjectRequest, file.toPath())

        return "https://${bucketName}.s3.amazonaws.com/${key}"
    }
}