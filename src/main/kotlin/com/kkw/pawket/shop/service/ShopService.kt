package com.kkw.pawket.shop.service

import com.kkw.pawket.ads.domain.repository.ShopRepository
import com.kkw.pawket.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ShopService(
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
) {
}