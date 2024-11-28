package com.kkw.petwalker.reservation.domain.repository

import com.kkw.petwalker.reservation.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository: JpaRepository<Reservation, String> {
}