package com.kkw.pawket.notice.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateNoticeReq(
    @Schema(description = "공지사항 제목", example = "긴급 공지!!")
    val title: String,

    @Schema(description = "공지사항 내용", example = "공지사항 내용입니다.")
    val content: String,

    @Schema(description = "공지사항 발송 대상", example = "all")
    val target: String,

    @Schema(description ="공지사항 필수 확인 여부", example = "true")
    val isRequired: Boolean,

    @Schema(description = "우선순위", example = "1")
    val priority: Int,
)
