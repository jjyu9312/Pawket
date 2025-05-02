package com.kkw.pawket.terms.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class TermsCreateReq(
    @Schema(description = "Title of the terms", example = "Terms of Service Agreement")
    val title: String,

    @Schema(description = "URL of the terms content", example = "http://pawket.com/terms.notion.site")
    val content: String,

    @Schema(description = "Whether the terms agreement is required", example = "true")
    val isRequired: Boolean,
)
