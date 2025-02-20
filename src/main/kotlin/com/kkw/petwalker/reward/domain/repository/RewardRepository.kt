package com.kkw.petwalker.reward.domain.repository

import com.kkw.petwalker.reward.domain.Reward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardRepository: JpaRepository<Reward, String> {
}