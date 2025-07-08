package com.kkw.pawket.partner.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class UpdatePartnerReq(
    @Schema(description = "Name of the partner company", example = "Milky Way Animal Hospital")
    val name: String,

    @Schema(description = "Name of the representative", example = "Kyungwook Kim")
    val ownerName: String,

    @Schema(description = "Contact number of the representative", example = "010-0000-0000")
    val contactNumber: String,

    @Schema(description = "Website or related link of the partner company", example = "www.google.com")
    val link: String? = null,
)
