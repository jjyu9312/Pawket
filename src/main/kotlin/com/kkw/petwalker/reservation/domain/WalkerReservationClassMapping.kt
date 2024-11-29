package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.Walker
import jakarta.persistence.*
import java.util.*

data class WalkerReservationClassMapping (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walker_id", columnDefinition = "CHAR(36)")
    val walker: Walker,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_class_id", columnDefinition = "CHAR(36)")
    val reservationClass: ReservationClass,
): BaseEntity()