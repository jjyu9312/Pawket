package com.kkw.pawket.partner.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateCompanyReq(
    @Schema(description = "Name of the partner company", example = "Milky Way Animal Hospital")
    val name: String,

    @Schema(description = "Basic address", example = "513, Yeongdong-daero, Gangnam-gu, Seoul")
    val addressBasic: String,

    @Schema(description = "Latitude of the address", example = "37.50915")
    val addressLat: Double,

    @Schema(description = "Longitude of the address", example = "127.06194")
    val addressLng: Double,

    @Schema(description = "Detailed address information", example = "COEX Hall D")
    val addressDetail: String? = null,
)
