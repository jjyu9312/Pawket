package com.kkw.pawket.ai.model.res

import com.kkw.pawket.ai.model.common.TokenUsage

data class PetChatRes(
    val reply: String,
    val sessionId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val tokenUsage: TokenUsage? = null
)