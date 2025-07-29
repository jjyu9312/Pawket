package com.kkw.pawket.shop.service


import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.shop.domain.Shop
import com.kkw.pawket.shop.domain.repository.ShopRepository
import com.kkw.pawket.shop.model.req.CreateShopReq
import com.kkw.pawket.shop.model.req.UpdateShopReq
import com.kkw.pawket.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ShopService(
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
) {
    fun createShop(userId: String, req: CreateShopReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val shop = Shop.create(
            user = user,
            name = req.name,
            addressBasic = req.addressBasic,
            addressLat = req.addressLat,
            addressLng = req.addressLng,
            addressDetail = req.addressDetail,
        )
        shopRepository.save(shop)

        return shop.id
    }

    fun updateShop(userId: String, shopId: String, req: UpdateShopReq) {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val shop = shopRepository.findByIdAndUserIdAndIsDeletedFalse(shopId, user.id)
            ?: throw BadRequestException(ResponseCode.SHOP_NOT_FOUND)

        shop.addressBasic = req.addressBasic
        shop.addressLat = req.addressLat
        shop.addressLng = req.addressLng
        shop.addressDetail = req.addressDetail

        shopRepository.save(shop)
    }

    fun deleteShop(userId: String, shopId: String) {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val shop = shopRepository.findByIdAndUserIdAndIsDeletedFalse(shopId, user.id)
            ?: throw BadRequestException(ResponseCode.SHOP_NOT_FOUND)

        shop.isDeleted = true

        shopRepository.save(shop)
    }
}