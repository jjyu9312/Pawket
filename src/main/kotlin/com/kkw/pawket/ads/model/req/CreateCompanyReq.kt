package com.kkw.pawket.partner.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateCompanyReq(
    @Schema(description = "제휴업체 이름", example = "은하수 동물병원")
    val name: String,

    @Schema(description = "주소 기본 정보", example = "서울특별시 강남구 영동대로 513")
    val addressBasic: String,

    @Schema(description = "주소 위도", example = "37.50915")
    val addressLat: Double,

    @Schema(description = "주소 경도", example = "127.06194")
    val addressLng: Double,

    @Schema(description = "주소 상세 정보", example = "코엑스 D홀")
    val addressDetail: String? = null,
)