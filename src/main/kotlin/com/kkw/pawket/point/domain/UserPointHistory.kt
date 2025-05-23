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
    val beforePoint: Int,

    @Column(nullable = false)
    val afterPoint: Int,

    ) : BaseEntity() {
        companion object {
            fun create(
                user: User,
                point: Point,
                beforePoint: Int,
                afterPoint: Int
            ): UserPointHistory {
                return UserPointHistory(
                    id = PointHistoryId(user.id, point.id),
                    user = user,
                    point = point,
                    beforePoint = beforePoint,
                    afterPoint = afterPoint,
                )
            }
        }
    }