package com.kkw.pawket.point.domain

import com.kkw.pawket.common.domain.BaseDateTimeEntity
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

    @Column()
    @Enumerated(EnumType.STRING)
    val type: PointHistoryType = PointHistoryType.COLLECT,

    @Column(nullable = false)
    var beforePoint: Int = 0,

    @Column(nullable = false)
    var afterPoint: Int = 0,

    ) : BaseEntity() {
        companion object {
            fun createByCollectPoint(
                user: User,
                point: Point,
            ): UserPointHistory {
                val beforePoint = user.totalPoint
                val afterPoint = beforePoint + point.petPoint

                return UserPointHistory(
                    id = PointHistoryId(user.id, point.id),
                    user = user,
                    point = point,
                    type = PointHistoryType.COLLECT,
                    beforePoint = beforePoint,
                    afterPoint = afterPoint
                )
            }

            fun createByUsePoint(
                user: User,
                point: Point,
            ): UserPointHistory {
                val beforePoint = user.totalPoint
                val afterPoint = beforePoint - point.petPoint

                return UserPointHistory(
                    id = PointHistoryId(user.id, point.id),
                    user = user,
                    point = point,
                    type = PointHistoryType.USE,
                    beforePoint = beforePoint,
                    afterPoint = afterPoint
                )
            }
        }
    }