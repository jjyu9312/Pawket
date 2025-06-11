package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class PointShopItem(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_shop_id", nullable = false, columnDefinition = "CHAR(36)")
    val pointShop: PointShop,

    @Column(nullable = false, length = 255)
    val name: String,

    @Column(nullable = false)
    val price: Int,

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PointShopItem
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            pointShop: PointShop,
            name: String,
            price: Int
        ): PointShopItem = PointShopItem(
            id = UUID.randomUUID().toString(),
            pointShop = pointShop,
            name = name,
            price = price
        )
    }
}