package com.kkw.pawket.reservation.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity(name = "reservation")
data class Reservation(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @JoinColumn(name = "hospital_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val partner: Partner,

    @Column(nullable = false)
    val reservationDate: Date,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: ReservationStatus = ReservationStatus.REQUESTED,

    @Column(nullable = false)
    val totalFee: Int = 0,

    @Column(nullable = false)
    val discountFee: Int = 0,
) : BaseEntity() {
    constructor(
        user: User,
        partner: Partner,
        reservationDate: Date,
        reservationStatus: ReservationStatus,
        totalFee: Int,
        discountFee: Int,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        partner = partner,
        reservationDate = reservationDate,
        status = reservationStatus,
        totalFee = totalFee,
        discountFee = discountFee,
    )
}