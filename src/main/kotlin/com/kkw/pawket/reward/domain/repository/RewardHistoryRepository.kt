package com.kkw.pawket.reward.domain.repository

import com.kkw.pawket.reward.domain.RewardHistory
import com.kkw.pawket.reward.domain.RewardHistoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardHistoryRepository: JpaRepository<RewardHistory, RewardHistoryId> {
    fun findAllByUserId(userId: String): List<RewardHistory>
}