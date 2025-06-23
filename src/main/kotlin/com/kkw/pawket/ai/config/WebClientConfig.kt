package com.kkw.pawket.ai.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun clovaWebClient(clovaConfig: ClovaConfig): WebClient {
        return WebClient.builder()
            .baseUrl(clovaConfig.baseUrl)
            .defaultHeader("Authorization", "Bearer ${clovaConfig.key}")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) // 2MB
            }
            .build()
    }

    @Bean
    fun generalWebClient(): WebClient {
        return WebClient.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024) // 1MB
            }
            .build()
    }
}