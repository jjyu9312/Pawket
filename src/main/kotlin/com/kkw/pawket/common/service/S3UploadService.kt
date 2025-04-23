package com.kkw.pawket.common.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@Service
class S3UploadService(
    private val s3Client: S3Client
) {
    @Value("\${cloud.aws.s3.bucket.name}")
    private lateinit var bucketName: String

    @Value("\${cloud.aws.s3.url-prefix.image}")
    private lateinit var imagePrefix: String

    @Value("\${cloud.aws.s3.url-prefix.pets}")
    private lateinit var petsPrefix: String

    /**
     * 단일 파일 업로드
     * @param file 업로드할 MultipartFile
     * @param dirName S3 내 디렉토리 이름
     * @return 업로드된 파일의 S3 URL
     */
    fun uploadSingleFile(file: MultipartFile?, dirName: String = "default"): String? {
        return file?.let {
            try {
                val originalFilename = it.originalFilename ?: return null
                val fileName = generateFileName(dirName, originalFilename)

                val putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(it.contentType)
                    .build()

                s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(it.inputStream, it.size)
                )

                "$imagePrefix${fileName.removePrefix("upload/image/")}"
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * 다중 파일 업로드
     * @param files 업로드할 MultipartFile 리스트
     * @param dirName S3 내 디렉토리 이름
     * @return 업로드된 파일들의 S3 URL 리스트
     */
    fun uploadMultipleFiles(files: List<MultipartFile>?, dirName: String = "default"): List<String> {
        return files?.mapNotNull {
            uploadSingleFile(it, dirName)
        } ?: emptyList()
    }

    /**
     * Pets 영상 업로드
     */
    fun uploadPetsVideoFile(file: MultipartFile?, dirName: String = "upload/pets"): String? {
        return file?.let {
            try {
                val originalFilename = it.originalFilename ?: return null
                val fileName = generateFileName(dirName, originalFilename)

                val putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(it.contentType)
                    .build()

                s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(it.inputStream, it.size)
                )

                "$petsPrefix${fileName.removePrefix("upload/pets/")}"
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * 파일명 생성 (중복 방지)
     */
    private fun generateFileName(dirName: String, originalFilename: String): String {
        val uuid = UUID.randomUUID().toString()
        return "$dirName/$uuid-$originalFilename"
    }

    /**
     * S3에서 파일 삭제
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    fun deleteFile(fileUrl: String?) {
        fileUrl?.let {
            val fileName = extractKeyFromUrl(it)
            val deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()

            s3Client.deleteObject(deleteObjectRequest)
        }
    }

    /**
     * S3 URL에서 파일 키 추출
     */
    private fun extractKeyFromUrl(fileUrl: String): String {
        return fileUrl.substringAfter("$bucketName.s3.amazonaws.com/")
    }
}