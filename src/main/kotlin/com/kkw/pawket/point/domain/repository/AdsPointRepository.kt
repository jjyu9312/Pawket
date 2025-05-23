package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.AdsPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsPointRepository : JpaRepository<AdsPoint, String> {
}