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

// Pet Profile
data class PetProfile(
    val id: String,
    val userId: String,
    val name: String,
    val type: String,  // "dog", "cat", etc.
    val dogType: String? = null,
    val age: Int,
    val weight: Int,
    val sex: String,
    val isNeutered: Boolean = false,
    val petDetail: String? = null,
)