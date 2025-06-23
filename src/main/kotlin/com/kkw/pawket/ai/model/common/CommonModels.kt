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
    val species: String,  // "dog", "cat", etc.
    val breed: String,
    val age: Int,
    val weight: Double? = null,
    val healthConditions: List<String> = emptyList(),
    val personality: List<String> = emptyList(),
    val allergies: List<String> = emptyList(),
    val medications: List<String> = emptyList()
)