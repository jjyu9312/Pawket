package com.kkw.pawket.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("WALK")
@PrimaryKeyJoinColumn(name = "reward_id")
class WalkReward(
    @Column(nullable = false)
    val distance: Double
) : Reward(
    coin = (distance * 100).toInt()
)

