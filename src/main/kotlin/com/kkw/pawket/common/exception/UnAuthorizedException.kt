package com.kkw.pawket.common.exception

import com.kkw.pawket.common.response.ResponseCode

class UnAuthorizedException(
    val responseCode: ResponseCode = ResponseCode.UNAUTHORIZED,
    val customMessage: String? = null
) : RuntimeException(customMessage ?: responseCode.defaultMessage) {
    // withCustomMessage와 유사한 메서드
    fun getFormattedMessage(): String {
        return responseCode.withCustomMessage(customMessage)
    }
}