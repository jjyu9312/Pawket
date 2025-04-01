package com.kkw.pawket.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("FEED")
@PrimaryKeyJoinColumn(name = "reward_id")
class FeedReward(
    @Column(nullable = false)
    val postCnt: Int = 0
) : Reward(
    coin = postCnt * 10
) {
}

