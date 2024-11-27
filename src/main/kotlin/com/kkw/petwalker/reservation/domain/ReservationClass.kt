package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.Walker
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
data class ReservationClass (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walker_id", columnDefinition = "CHAR(36)")
    val walker: Walker,

    @Column(nullable = false)
    val startDate: LocalDate,

    @Column(nullable = false)
    val endDate: LocalDate,

    @Column(nullable = false)
    val startTime: LocalTime,

    @Column(nullable = false)
    val endTime: LocalTime,
): BaseEntity()