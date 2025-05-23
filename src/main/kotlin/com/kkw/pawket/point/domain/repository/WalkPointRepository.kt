package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.WalkPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkPointRepository : JpaRepository<WalkPoint, String> {
}