package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.FeedPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedPointRepository : JpaRepository<FeedPoint, String> {
}