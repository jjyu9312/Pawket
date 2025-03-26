package com.kkw.pawket.reservation.domain.repository

import com.kkw.pawket.reservation.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository: JpaRepository<Reservation, String> {
}