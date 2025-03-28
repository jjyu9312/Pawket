package com.kkw.pawket.reward.domain.repository

import com.kkw.pawket.reward.domain.WalkReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRewardRepository : JpaRepository<WalkReward, String> {
}