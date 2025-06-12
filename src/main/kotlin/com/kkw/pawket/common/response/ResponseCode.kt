package com.kkw.pawket.common.response

enum class ResponseCode(val code: Int, val defaultMessage: String) {
    // 200
    SUCCESS(20000, "요청이 성공했습니다"),

    // 400
    BAD_REQUEST(40000, "잘못된 요청입니다"),
    USER_CREATION_FAILED(40001, "user 생성에 실패했습니다"),
    DOG_CREATION_FAILED(40003, "dog 생성에 실패했습니다"),
    NOT_FOUND_IMAGE(40004, "이미지를 찾을 수 없습니다"),
    USER_NOT_FOUND(40010, "user가 존재하지 않습니다"),
    COMPANY_NOT_FOUND(40011, "company가 존재하지 않습니다"),
    WALK_RECORD_NOT_FOUND(40012, "산책 기록이 존재하지 않습니다"),
    EMAIL_NOT_FOUND(40013, "이메일을 찾을 수 없습니다"),
    TERMS_NOT_FOUND(40014, "약관을 찾을 수 없습니다"),
    NOTICE_NOT_FOUND(40015, "공지사항을 찾을 수 없습니다"),
    POINT_SHOP_NOT_FOUND(40020, "포인트샵을 찾을 수 없습니다"),
    POINT_SHOP_ITEM_NOT_FOUND(40021, "포인트샵 아이템을 찾을 수 없습니다"),
    POINT_SHOP_NAME_DUPLICATED(40016, "포인트샵 이름이 중복되었습니다"),
    POINT_SHOP_ITEM_NAME_DUPLICATED(40017, "포인트샵 아이템 이름이 중복되었습니다"),
    // NOTFOUND



    INVALID_SEX_TYPE(40030, "강아지 성별 유형이 맞지 않습니다"),
    INVALID_GENDER_TYPE(40031, "성별 유형이 맞지 않습니다"),
    INVALID_DOG_TYPE(40032, "강아지 종류가 맞지 않습니다"),
    INVALID_EMAIL_FORMAT(40033, "이메일 포맷이 맞지 않습니다"),
    INVALID_TARGET_GROUP(40034, "해당 타겟 그룹은 존재하지 않습니다"),
    INVALID_OAUTH_PROVIDER(40035, "해당 Oauth 제공자는 존재하지 않습니다"),
    INVALID_OAUTH_PROVIDER_ENDPOINT(40036, "해당 Oauth 제공자 엔드포인트는 존재하지 않습니다"),
    INVALID_PET_TYPE(40037, "펫 종류가 맞지 않습니다"),
    INVALID_POINT_TYPE(40038, "포인트 타입이 맞지 않습니다"),
    // INVALID



    ALREADY_AGREED_TERMS(40060, "이미 동의한 약관은 다시 동의할 수 없습니다"),
    WALK_POINT_LIMIT_EXCEEDED(40061, "산책 포인트는 4회까지만 적립할 수 있습니다"),
    ADS_POINT_LIMIT_EXCEEDED(40062, "광고 포인트는 2회까지만 적립할 수 있습니다"),

    //401
    UNAUTHORIZED(40100, "인증되지 않은 사용자입니다"),
    INVALID_TOKEN(40101, "유효하지 않은 토큰입니다"),
    OAUTH_USERINFO_INVALID(40103, "Oauth 유저 정보가 유효하지 않습니다"),

    // 500
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