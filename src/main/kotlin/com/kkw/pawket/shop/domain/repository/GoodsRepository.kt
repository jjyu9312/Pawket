package com.kkw.pawket.shop.domain.repository

import com.kkw.pawket.shop.domain.Goods
import com.kkw.pawket.shop.domain.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoodsRepository: JpaRepository<Goods, String> {
    fun findAllByShopIn(companies: List<Shop>): List<Goods>
}