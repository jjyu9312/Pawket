package com.kkw.petwalker.reward.domain.repository

import com.kkw.petwalker.reward.domain.AdsReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsRewardRepository : JpaRepository<AdsReward, String> {
}