package com.kkw.petwalker.reward.domain.repository

import com.kkw.petwalker.reward.domain.RankReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RankRewardRepository : JpaRepository<RankReward, String> {
}