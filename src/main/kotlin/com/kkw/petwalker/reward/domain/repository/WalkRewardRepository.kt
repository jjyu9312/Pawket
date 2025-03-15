package com.kkw.petwalker.reward.domain.repository

import com.kkw.petwalker.reward.domain.WalkReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRewardRepository : JpaRepository<WalkReward, String> {
}