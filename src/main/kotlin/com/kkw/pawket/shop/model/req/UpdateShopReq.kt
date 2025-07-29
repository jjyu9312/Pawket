package com.kkw.pawket.shop.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateShopReq(
    @Schema(description = "address of the shop", example = "서울시 강남구 테헤란로 123")
    val addressBasic: String,

    @Schema(description = "Latitude of the shop address", example = "37.123456")
    val addressLat: Double,

    @Schema(description = "Longitude of the shop address", example = "127.123456")
    val addressLng: Double,

    @Schema(description = "Detailed address of the shop", example = "4층")
    val addressDetail: String,
)
