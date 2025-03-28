package com.kkw.pawket.spot.domain.repository

import com.kkw.pawket.spot.domain.Spot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpotRepository: JpaRepository<Spot, String> {
}