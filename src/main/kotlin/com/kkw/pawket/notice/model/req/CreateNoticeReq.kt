package com.kkw.pawket.notice.model.req

data class CreateNoticeReq(
    val title: String,
    val content: String,
    val target: String,
    val isRequired: Boolean,
    val priority: Int,
)
