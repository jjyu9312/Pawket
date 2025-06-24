package com.kkw.pawket.ai.model.res

import com.kkw.pawket.ai.model.common.ChatMessage

// CLOVA 네이티브 API - Response
data class ClovaRes(
    val status: Status,
    val result: Result
) {
    data class Status(
        val code: String,
        val message: String
    )

    data class Result(
        val message: ChatMessage,
        val inputLength: Int,
        val outputLength: Int,
        val stopReason: String
    )
}