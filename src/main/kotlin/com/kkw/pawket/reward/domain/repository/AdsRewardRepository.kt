package com.kkw.pawket.reward.domain.repository

import com.kkw.pawket.reward.domain.AdsReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsRewardRepository : JpaRepository<AdsReward, String> {
}