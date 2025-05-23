package com.kkw.pawket.point.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class PointHistoryId(
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(name = "point_id", columnDefinition = "CHAR(36)")
    val pointId: String
) : java.io.Serializable
