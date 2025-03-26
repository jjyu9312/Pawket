package com.kkw.pawket.common.service

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.oauth")
data class OAuthProviderProperties(
    val google: ProviderInfo,
    val kakao: ProviderInfo,
    val apple: ProviderInfo
) {
    data class ProviderInfo(
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String,
        val scope: List<String>
    )
}
