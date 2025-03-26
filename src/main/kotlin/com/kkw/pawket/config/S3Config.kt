package com.kkw.pawket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config {

    @Value("\${cloud.aws.s3.credentials.accessKey}")
    private lateinit var accessKey: String

    @Value("\${cloud.aws.s3.credentials.secretKey}")
    private lateinit var secretKey: String

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            accessKey,
            secretKey
        )

        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2) // 사용 중인 AWS 리전
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}
