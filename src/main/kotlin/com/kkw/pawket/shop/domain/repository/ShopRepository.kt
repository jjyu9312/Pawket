package com.kkw.pawket.shop.domain.repository

import com.kkw.pawket.shop.domain.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShopRepository: JpaRepository<Shop, String> {
    fun findByIdAndIsDeletedFalse(id: String): Shop?
    fun findAllByUserId(userId: String): List<Shop>
}