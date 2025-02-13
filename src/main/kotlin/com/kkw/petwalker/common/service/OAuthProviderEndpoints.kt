package com.kkw.petwalker.common.service

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider")
data class OAuthProviderEndpoints(
    val google: ProviderEndpoints,
    val kakao: ProviderEndpoints,
    val apple: ProviderEndpoints
) {
    data class ProviderEndpoints(
        val authorizationUri: String,
        val tokenUri: String,
        val userInfoUri: String
    )
}
