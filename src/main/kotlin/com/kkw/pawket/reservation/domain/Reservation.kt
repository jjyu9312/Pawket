package com.kkw.pawket.reservation.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Reservation
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            partner: Partner,
            reservationDate: Date,
            status: ReservationStatus,
            totalFee: Int,
            discountFee: Int,
        ): Reservation = Reservation(
            user = user,
            partner = partner,
            reservationDate = reservationDate,
            status = status,
            totalFee = totalFee,
            discountFee = discountFee
        )
    }
}