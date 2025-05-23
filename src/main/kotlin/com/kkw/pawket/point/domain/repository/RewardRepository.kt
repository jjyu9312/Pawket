package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointRepository : JpaRepository<Point, String> {
}