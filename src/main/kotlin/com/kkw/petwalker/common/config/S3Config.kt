package com.kkw.petwalker.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config {

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(
            "YOUR_ACCESS_KEY", // AWS Access Key
            "YOUR_SECRET_KEY"  // AWS Secret Key
        )

        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2) // 사용 중인 AWS 리전
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}
