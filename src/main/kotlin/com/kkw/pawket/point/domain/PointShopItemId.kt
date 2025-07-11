package com.kkw.pawket.point.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class PointShopItemId(
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(name = "point_shop_item_id", columnDefinition = "CHAR(36)")
    val pointShopItemId: String
) : java.io.Serializable
