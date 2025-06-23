package com.kkw.pawket.ai.model.res

// 서비스 상태 - Response
data class HealthCheckRes(
    val status: String,
    val service: String,
    val timestamp: Long = System.currentTimeMillis(),
    val version: String? = null,
    val uptime: Long? = null
)