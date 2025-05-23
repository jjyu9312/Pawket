package com.kkw.pawket.point.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
@DiscriminatorValue("WALK")
@PrimaryKeyJoinColumn(name = "point_id")
class WalkPoint(
    @Column(nullable = false)
    val distance: Double
) : Point(
    coin = (distance * 100).toInt()
)

