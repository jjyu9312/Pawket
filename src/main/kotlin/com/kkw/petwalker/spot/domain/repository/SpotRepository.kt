package com.kkw.petwalker.spot.domain.repository

import com.kkw.petwalker.spot.domain.Spot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpotRepository: JpaRepository<Spot, String> {
}