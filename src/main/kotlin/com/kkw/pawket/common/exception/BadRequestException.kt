package com.kkw.pawket.common.exception

import com.kkw.pawket.common.response.ResponseCode

class BadRequestException(
    val responseCode: ResponseCode,
    val customMessage: String? = null
) : RuntimeException(customMessage ?: responseCode.defaultMessage) {
    fun getFormattedMessage(): String {
        return responseCode.withCustomMessage(customMessage)
    }
}