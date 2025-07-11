package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*

@Entity
data class UserPointShopItemHistory(
    @EmbeddedId
    val id: PointShopItemId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @MapsId("pointId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_shop_item_id", columnDefinition = "CHAR(36)")
    val pointShopItem: PointShopItem,

    ) : BaseEntity() {
        companion object {
            fun create(
                user: User,
                pointShopItem: PointShopItem,
            ): UserPointShopItemHistory {
                return UserPointShopItemHistory(
                    id = PointShopItemId(user.id, pointShopItem.id),
                    user = user,
                    pointShopItem = pointShopItem,
                )
            }
        }
    }