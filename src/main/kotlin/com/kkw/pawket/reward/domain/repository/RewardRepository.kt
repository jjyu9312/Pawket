package com.kkw.pawket.reward.domain.repository

import com.kkw.pawket.reward.domain.Reward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardRepository : JpaRepository<Reward, String> {
}