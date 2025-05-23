package com.kkw.pawket.point.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("FEED")
@PrimaryKeyJoinColumn(name = "point_id")
class FeedPoint(
    @Column(nullable = false)
    val postCnt: Int = 0
) : Point(
    coin = postCnt * 10
) {
}

