package com.kkw.pawket.terms.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class TermsCreateReq(
    @Schema(description = "약관 명", example = "이용 약관 동의")
    val title: String,

    @Schema(description = "약관 동의 내용 URL", example = "http://pawket.com/terms.notion.site")
    val content: String,

    @Schema(description = "필수 약관 동의 여부", example = "true")
    val isRequired: Boolean,
)