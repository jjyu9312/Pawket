package com.kkw.pawket.reward.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class RewardHistory(
    @EmbeddedId
    val id: RewardHistoryId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @MapsId("rewardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", columnDefinition = "CHAR(36)")
    val reward: Reward,

    @Column(nullable = false)
    val beforeCoin: Int,

    @Column(nullable = false)
    val afterCoin: Int,

    ) : BaseEntity()