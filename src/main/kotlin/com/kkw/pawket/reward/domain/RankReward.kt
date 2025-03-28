package com.kkw.pawket.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("RANK")
@PrimaryKeyJoinColumn(name = "reward_id")
class RankReward(
    @Column(nullable = false)
    val rank: Int
) : Reward(
    coin = calculateCoin(rank)
) {
    companion object {
        fun calculateCoin(rank: Int): Int {
            return when (rank) {
                1 -> 500
                2 -> 300
                3 -> 100
                else -> 0
            }
        }
    }
}
