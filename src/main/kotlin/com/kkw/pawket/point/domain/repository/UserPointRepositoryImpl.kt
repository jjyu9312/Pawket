package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.PointType
import com.kkw.pawket.point.domain.QUserPointHistory.userPointHistory
import com.kkw.pawket.point.domain.QPoint.point1
import com.kkw.pawket.user.domain.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserPointRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : UserPointRepositoryCustom {
    override fun countWalkPointHistoryByUserAndType(user: User, type: PointType): Long {
        return jpaQueryFactory
            .select(userPointHistory.count())
            .from(userPointHistory)
            .on(userPointHistory.point.id.eq(point1.id))
            .where(
                userPointHistory.user.eq(user)
                    .and(point1.type.eq(type))
            )
            .fetchOne() ?: 0L
    }
}