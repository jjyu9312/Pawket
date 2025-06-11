package com.kkw.pawket.point.service

import com.kkw.pawket.point.model.req.CreatePointShopItemReq
import com.kkw.pawket.point.model.req.CreatePointShopReq
import com.kkw.pawket.point.model.res.ReadPointShopRes
import org.springframework.stereotype.Service

@Service
class PointShopService {
    fun createPointShop(userId: String, req: CreatePointShopReq): String {
        TODO("Not yet implemented")
    }

    fun createPointShopItem(userId: String, req: CreatePointShopItemReq): String {
        TODO("Not yet implemented")
    }

    fun getPointShopsByUser(userId: String): List<ReadPointShopRes>? {
        TODO("Not yet implemented")
    }
}