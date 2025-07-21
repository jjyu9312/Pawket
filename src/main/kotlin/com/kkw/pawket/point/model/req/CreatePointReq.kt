package com.kkw.pawket.point.model.req

data class CreatePointReq(
    val type: String,
    val pointHistoryType: String,
    val point: Int,
)
