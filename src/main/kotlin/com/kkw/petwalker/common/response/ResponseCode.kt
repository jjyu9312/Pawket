package com.kkw.petwalker.common.response

enum class ResponseCode(val code: Int, val defaultMessage: String) {
    SUCCESS(20000, "요청이 성공했습니다"),
    BAD_REQUEST(40000, "잘못된 요청입니다"),
    USER_CREATION_FAILED(40001, "user 생성에 실패했습니다"),
    OWNER_CREATION_FAILED(40002, "owner 생성에 실패했습니다"),
    WALKER_CREATION_FAILED(40003, "walker 생성에 실패했습니다"),
    NOT_FOUND(40400, "리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(50000, "서버 내부 오류가 발생했습니다"),
    ;

    fun withCustomMessage(customMessage: String?): String {
        return if (customMessage.isNullOrBlank()) defaultMessage else "$defaultMessage: $customMessage"
    }

    companion object {
        fun fromCode(code: Int): ResponseCode? {
            return entries.find { it.code == code }
        }
    }
}