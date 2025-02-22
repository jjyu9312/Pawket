package com.kkw.petwalker.reward.domain

import com.kkw.petwalker.ads.Company
import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class RewardHistory(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val company: Company,

    // TODO : RewardHistory에 사용한 항목 필드 추가

    @Column(nullable = false)
    val beforeCoin: Int,

    @Column(nullable = false)
    val afterCoin: Int,

    ) : BaseEntity()