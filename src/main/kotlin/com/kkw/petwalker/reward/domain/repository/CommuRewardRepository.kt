package com.kkw.petwalker.reward.domain.repository

import com.kkw.petwalker.reward.domain.CommuReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommuRewardRepository : JpaRepository<CommuReward, String> {
}