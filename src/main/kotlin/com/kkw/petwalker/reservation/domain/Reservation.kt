package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.Walker
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Reservation(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_class_id", columnDefinition = "CHAR(36)")
    val reservationClass: ReservationClass,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walker_id", columnDefinition = "CHAR(36)")
    val walker: Walker,

    @Column(columnDefinition = "CHAR(36)")
    val dogId: String,

    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime,

    ) : BaseEntity() {
    constructor(
        reservationClass: ReservationClass,
        walker: Walker,
        dogId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ) : this(
        id = UUID.randomUUID().toString(),
        reservationClass = reservationClass,
        walker = walker,
        dogId = dogId,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
    )
}