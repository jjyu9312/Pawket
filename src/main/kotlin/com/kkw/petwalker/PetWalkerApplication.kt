package com.kkw.petwalker

import com.kkw.petwalker.common.service.OAuthProviderEndpoints
import com.kkw.petwalker.common.service.OAuthProviderProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OAuthProviderProperties::class, OAuthProviderEndpoints::class)
class PetWalkerApplication

fun main(args: Array<String>) {
    runApplication<PetWalkerApplication>(*args)
}
