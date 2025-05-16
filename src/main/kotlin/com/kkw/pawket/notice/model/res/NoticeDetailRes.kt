package com.kkw.pawket.notice.model.res

data class NoticeDetailRes (
    val id: String,
    val title: String,
    val content: String,
    val targetGroup: String,
    val isRequired: Boolean,
    val priority: Int,
)