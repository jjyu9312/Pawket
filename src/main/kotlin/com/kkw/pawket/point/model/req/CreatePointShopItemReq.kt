package com.kkw.pawket.point.model.req

data class CreatePointShopItemReq(
    val pointShopId: String,
    val name: String,
    val price: Int,
)
