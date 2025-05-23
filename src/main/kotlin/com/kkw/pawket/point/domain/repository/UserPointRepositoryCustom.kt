package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.PointType
import com.kkw.pawket.user.domain.User

interface UserPointRepositoryCustom {
    fun countWalkPointHistoryByUserAndType(user: User, type: PointType): Long
}