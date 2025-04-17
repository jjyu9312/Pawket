package com.kkw.pawket.shop.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Goods(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val shop: Shop,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = true)
    val content: String? = null,

    @Column(nullable = true)
    val coin: Int = 0,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Goods
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            shop: Shop,
            title: String,
            content: String?,
            coin: Int,
        ): Goods = Goods(
            shop = shop,
            title = title,
            content = content,
            coin = coin,
        )
    }
}
