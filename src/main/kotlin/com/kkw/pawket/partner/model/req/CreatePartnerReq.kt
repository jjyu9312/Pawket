package com.kkw.pawket.partner.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreatePartnerReq(
    @Schema(description = "제휴업체 이름", example = "은하수 동물병원")
    val name: String,

    @Schema(description = "제휴업체 대표 이름", example = "김경욱")
    val ownerName: String,

    @Schema(description = "제휴업체 대표 번호", example = "010-0000-0000")
    val contactNumber: String,

    @Schema(description = "제휴업체 관련 사이트", example = "www.google.com")
    val link: String,
)