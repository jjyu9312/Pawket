package com.kkw.petwalker.reservation.domain.repository

import com.kkw.petwalker.reservation.domain.WalkerReservationClassMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkerReservationClassMappingRepository: JpaRepository<WalkerReservationClassMapping, String> {
}