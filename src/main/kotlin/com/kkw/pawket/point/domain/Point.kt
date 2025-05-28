package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Point(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    val type: PointType = PointType.WALK,

    @Column(nullable = false)
    var point: Int,
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Point
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(point: Int, type: PointType): Point = Point(
            id = UUID.randomUUID().toString(),
            type = type,
            point = point
        )
    }
}