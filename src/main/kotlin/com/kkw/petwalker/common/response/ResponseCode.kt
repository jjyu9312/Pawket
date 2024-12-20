package com.kkw.petwalker.common.response

enum class ResponseCode(val code: Int, val defaultMessage: String) {
    SUCCESS(20000, "Request successful"),
    BAD_REQUEST(40000, "Invalid request"),
    NOT_FOUND(40400, "Resource not found"),
    INTERNAL_SERVER_ERROR(50000, "Internal server error"),
    WALKER_CREATION_FAILED(50001, "Failed to create walker");

    fun withCustomMessage(customMessage: String?): String {
        return if (customMessage.isNullOrBlank()) defaultMessage else "$defaultMessage: $customMessage"
    }

    companion object {
        fun fromCode(code: Int): ResponseCode? {
            return entries.find { it.code == code }
        }
    }
}