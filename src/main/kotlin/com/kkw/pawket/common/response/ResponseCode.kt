package com.kkw.pawket.common.response

enum class ResponseCode(val code: Int, val defaultMessage: String) {
    SUCCESS(20000, "요청이 성공했습니다"),
    BAD_REQUEST(40000, "잘못된 요청입니다"),
    USER_CREATION_FAILED(40001, "user 생성에 실패했습니다"),
    DOG_CREATION_FAILED(40003, "dog 생성에 실패했습니다"),
    INVALID_SEX_TYPE(40004, "강아지 성별 유형이 맞지 않습니다"),
    INVALID_GENDER_TYPE(40005, "성별 유형이 맞지 않습니다."),
    INVALID_DOG_TYPE(40006, "강아지 종류가 맞지 않습니다"),
    NOT_FOUND_IMAGE(40007, "이미지를 찾을 수 없습니다"),
    INVALID_EMAIL_FORMAT(40008, "이메일 포맷이 맞지 않습니다"),
    USER_NOT_FOUND(40009, "user가 존재하지 않습니다"),
    COMPANY_NOT_FOUND(40010, "company가 존재하지 않습니다"),
    INTERNAL_SERVER_ERROR(50000, "서버 내부 오류가 발생했습니다"),
    OAUTH_TOKEN_INVALID(50001, "Oauth 토큰이 유효하지 않습니다"),
    OAUTH_USERINFO_INVALID(50002, "Oauth 유저 정보가 유효하지 않습니다"),
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