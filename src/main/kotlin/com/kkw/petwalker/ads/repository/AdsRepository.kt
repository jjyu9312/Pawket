package com.kkw.petwalker.ads.repository

import com.kkw.petwalker.ads.Ads
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsRepository: JpaRepository<Ads, String> {
}