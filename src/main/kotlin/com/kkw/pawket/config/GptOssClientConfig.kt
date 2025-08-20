package com.kkw.pawket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GptOssClientConfig {

    /**
     * baseUrl: vLLM/OpenAI-호환 엔드포인트 (예: http://model-svc.internal:8000/v1)
     * internalToken: 내부망 토큰 사용 시만 세팅(없으면 헤더 추가 안 함)
     */
    @Bean
    fun ossWebClient(
        builder: WebClient.Builder,
        @Value("\${oss.base-url}") baseUrl: String,
        @Value("\${oss.internal-token:}") internalToken: String?
    ): WebClient = builder
        .baseUrl(baseUrl)
        .defaultHeaders { headers ->
            if (!internalToken.isNullOrBlank()) {
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer $internalToken")
            }
        }
        .build()
}