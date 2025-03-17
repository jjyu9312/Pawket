package com.kkw.petwalker.spot.model.req

data class CreateSpotReq(
    val name: String,
    val detail: String,
    val importanceLevel: String,
    val addressBasic: String,
    val addressLat: Double,
    val addressLng: Double,
    val addressDetail: String
)
