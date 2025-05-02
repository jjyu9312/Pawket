package com.kkw.pawket.notice.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateNoticeReq(
    @Schema(description = "Title of the notice", example = "Urgent Notice!!")
    val title: String,

    @Schema(description = "Content of the notice", example = "This is the content of the notice.")
    val content: String,

    @Schema(description = "Target audience for the notice", example = "all")
    val target: String,

    @Schema(description = "Whether the notice requires mandatory confirmation", example = "true")
    val isRequired: Boolean,

    @Schema(description = "Priority of the notice", example = "1")
    val priority: Int,
)
