package com.kkw.pawket.point.domain.repository

import com.kkw.pawket.point.domain.PointShop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointShopRepository : JpaRepository<PointShop, String> {
    fun findByIdAndIsDeletedFalse(id: String): PointShop?
    fun existsByNameAndIsDeletedFalse(name: String): Boolean
}