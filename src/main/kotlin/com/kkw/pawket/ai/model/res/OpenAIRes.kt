package com.kkw.pawket.ai.model.res

import com.fasterxml.jackson.annotation.JsonProperty
import com.kkw.pawket.ai.model.common.ChatMessage

// OpenAI 호환 API - Response
data class OpenAIRes(
    val id: String? = null,
    val `object`: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<Choice>,
    val usage: Usage? = null
) {
    data class Choice(
        val index: Int = 0,
        val message: ChatMessage,
        @JsonProperty("finish_reason")
        val finishReason: String? = null
    )

    data class Usage(
        @JsonProperty("prompt_tokens")
        val promptTokens: Int,
        @JsonProperty("completion_tokens")
        val completionTokens: Int,
        @JsonProperty("total_tokens")
        val totalTokens: Int
    )
}