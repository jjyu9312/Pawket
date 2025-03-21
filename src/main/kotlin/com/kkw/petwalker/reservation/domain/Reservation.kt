package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.hospital.domain.Hospital
import com.kkw.petwalker.user.domain.User
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
    val hospital: Hospital,

    @Column(nullable = false)
    val reservationDate: Date,

    @Column(nullable = false)
    val totalFee: Int = 0,
) : BaseEntity() {
    constructor(
        user: User,
        hospital: Hospital,
        reservationDate: Date,
        totalFee: Int,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        hospital = hospital,
        reservationDate = reservationDate,
        totalFee = totalFee,
    )
}