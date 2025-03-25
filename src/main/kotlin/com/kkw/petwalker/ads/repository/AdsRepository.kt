package com.kkw.pawket.ads.repository

import com.kkw.pawket.ads.Ads
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdsRepository: JpaRepository<Ads, String> {
}