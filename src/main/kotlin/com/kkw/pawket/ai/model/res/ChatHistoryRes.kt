package com.kkw.pawket.ai.model.res

import com.kkw.pawket.ai.model.common.PetProfile

// 채팅 히스토리 - Response
data class ChatHistoryRes(
    val sessionId: String,
    val messages: List<ChatMessageRes>,
    val totalCount: Int
)

data class ChatMessageRes(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
