package com.kkw.pawket.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("FEED")
@PrimaryKeyJoinColumn(name = "reward_id")  // 부모 테이블과 PK를 공유
class FeedReward(
    @Column(nullable = false)
    val postCnt: Int
) : Reward(
    coin = postCnt * 10
)

