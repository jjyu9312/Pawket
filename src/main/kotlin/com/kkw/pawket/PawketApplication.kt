package com.kkw.pawket

import com.kkw.pawket.common.service.OAuthProviderEndpoints
import com.kkw.pawket.common.service.OAuthProviderProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    OAuthProviderProperties::class,
    OAuthProviderEndpoints::class
)
class pawketApplication

fun main(args: Array<String>) {
    runApplication<pawketApplication>(*args)
}