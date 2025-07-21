package com.kkw.pawket.point.service

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.point.domain.PointShop
import com.kkw.pawket.point.domain.PointShopItem
import com.kkw.pawket.point.domain.repository.PointShopItemRepository
import com.kkw.pawket.point.domain.repository.PointShopRepository
import com.kkw.pawket.point.model.req.CreatePointShopItemReq
import com.kkw.pawket.point.model.req.CreatePointShopReq
import com.kkw.pawket.point.model.res.ReadPointShopRes
import com.kkw.pawket.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PointShopService(
    private val userRepository: UserRepository,
    private val pointShopRepository: PointShopRepository,
    private val pointShopItemRepository: PointShopItemRepository,
) {
    fun createPointShop(userId: String, req: CreatePointShopReq): String {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId))
            throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        if (pointShopRepository.existsByNameAndIsDeletedFalse(req.name))
            throw BadRequestException(ResponseCode.POINT_SHOP_NAME_DUPLICATED)

        val pointShop = PointShop.create(
            name = req.name
        )
        pointShopRepository.save(pointShop)

        return pointShop.id
    }

    fun createPointShopItem(userId: String, req: CreatePointShopItemReq): String {
        if (userRepository.existsByIdAndIsDeletedFalse(userId))
            throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        if (!pointShopItemRepository.existsByNameAndIsDeletedFalse(req.pointShopId))
            throw BadRequestException(ResponseCode.POINT_SHOP_ITEM_NAME_DUPLICATED)

        val pointShop = pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId)
            ?: throw BadRequestException(ResponseCode.POINT_SHOP_NOT_FOUND)

        val pointShopItem = PointShopItem.create(
            pointShop = pointShop,
            name = req.name,
            price = req.price,
        )
        pointShopItemRepository.save(pointShopItem)

        return pointShopItem.id
    }

//    fun getPointShopsByUser(userId: String): List<ReadPointShopRes>? {
//        val user = userRepository.findByIdAndIsDeletedFalse(userId)
//            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)
//
//
//    }
}