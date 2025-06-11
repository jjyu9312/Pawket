package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.PointShopItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointShopItemRepository : JpaRepository<PointShopItem, String> {
}