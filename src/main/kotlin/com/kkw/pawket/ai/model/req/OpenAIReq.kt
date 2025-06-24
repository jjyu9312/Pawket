package com.kkw.pawket.ai.model.req

import com.fasterxml.jackson.annotation.JsonProperty
import com.kkw.pawket.ai.model.common.ChatMessage

// OpenAI 호환 API - Request
data class OpenAIReq(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    @JsonProperty("max_tokens")
    val maxTokens: Int = 1000,
    val stream: Boolean = false
)