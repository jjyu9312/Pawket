package com.kkw.pawket.reward.domain.repository

import com.kkw.pawket.reward.domain.FeedReward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRewardRepository : JpaRepository<FeedReward, String> {
}