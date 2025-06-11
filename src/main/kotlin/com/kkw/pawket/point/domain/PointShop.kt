package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class PointShop(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, length = 255)
    val name: String,

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PointShop
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(name: String): PointShop = PointShop(
            id = UUID.randomUUID().toString(),
            name = name
        )
    }
}