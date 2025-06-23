package com.kkw.pawket.ai.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "clova-ai.open-api")
data class ClovaConfig(
    var key: String = "",
    var baseUrl: String = "https://api.ncloud.com/v1/ai/clova",
    var defaultModel: String = "HCX-005",
    var timeout: Long = 30000L,
    var maxRetries: Int = 3
)