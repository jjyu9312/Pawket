package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.UserPointHistory
import com.kkw.pawket.point.domain.PointHistoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPointHistoryRepository: JpaRepository<UserPointHistory, PointHistoryId>, UserPointRepositoryCustom {
    fun findAllByUserId(userId: String): List<UserPointHistory>
}