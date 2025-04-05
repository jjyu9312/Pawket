package com.kkw.pawket.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class RewardHistoryId(
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(name = "reward_id", columnDefinition = "CHAR(36)")
    val rewardId: String
) : java.io.Serializable
