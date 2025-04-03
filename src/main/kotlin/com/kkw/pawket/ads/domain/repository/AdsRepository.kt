package com.kkw.pawket.ads.domain.repository

import com.kkw.pawket.ads.domain.Ads
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsRepository: JpaRepository<Ads, String> {
    fun findAllByUserId(userId: String): List<Ads>
}