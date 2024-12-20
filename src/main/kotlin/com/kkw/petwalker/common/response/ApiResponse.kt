package com.kkw.petwalker.common.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val data: T?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
