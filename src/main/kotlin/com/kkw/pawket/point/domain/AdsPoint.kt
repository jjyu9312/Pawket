package com.kkw.pawket.point.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("AD")
@PrimaryKeyJoinColumn(name = "point_id") // 부모 테이블과 PK를 공유
class AdsPoint(
    @Column(nullable = false)
    val adsPrice: Int = 0
) : Point(
    coin = adsPrice
) {
}
