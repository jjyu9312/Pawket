package com.kkw.pawket.ai.model.common

// 공통 ChatMessage
data class ChatMessage(
    val role: String, // "system", "user", "assistant"
    val content: String
)

// Token 사용량 추적
data class TokenUsage(
    val inputTokens: Int,
    val outputTokens: Int,
    val totalTokens: Int
)

// Dog Profile
data class DogProfile(
    val id: String,
    val userId: String,
    val name: String,
    val type: String,
    val age: Int,
    val weight: Int,
    val sex: String,
    val isNeutered: Boolean = false,
    val dogDetail: String? = null,
)