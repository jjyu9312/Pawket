package com.kkw.pawket.reward.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class RewardHistory(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", columnDefinition = "CHAR(36)")
    val reward: Reward,

    @Column(nullable = false)
    val beforeCoin: Int,

    @Column(nullable = false)
    val afterCoin: Int,

    ) : BaseEntity()