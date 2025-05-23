package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*

@Entity
data class UserPointHistory(
    @EmbeddedId
    val id: PointHistoryId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @MapsId("pointId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", columnDefinition = "CHAR(36)")
    val point: Point,

    @Column(nullable = false)
    val beforeCoin: Int,

    @Column(nullable = false)
    val afterCoin: Int,

    ) : BaseEntity()