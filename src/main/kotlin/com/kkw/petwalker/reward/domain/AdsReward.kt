package com.kkw.petwalker.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("RANK")
@PrimaryKeyJoinColumn(name = "reward_id")
class AdsReward(
    @Column(nullable = false)
    val adsPrice: Int
) : Reward(
    coin = adsPrice
)
