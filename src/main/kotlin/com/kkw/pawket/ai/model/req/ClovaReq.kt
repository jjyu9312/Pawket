package com.kkw.pawket.ai.model.req

import com.kkw.pawket.ai.model.common.ChatMessage

// CLOVA 네이티브 API - Request
data class ClovaReq(
    val messages: List<ChatMessage>,
    val topP: Double = 0.8,
    val topK: Int = 0,
    val maxTokens: Int = 256,
    val temperature: Double = 0.5,
    val repeatPenalty: Double = 5.0,
    val stopBefore: List<String> = emptyList(),
    val includeAiFilters: Boolean = true
)