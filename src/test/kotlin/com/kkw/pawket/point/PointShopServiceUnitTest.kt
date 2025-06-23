package com.kkw.pawket.point

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.point.domain.PointShop
import com.kkw.pawket.point.domain.PointShopItem
import com.kkw.pawket.point.domain.repository.PointShopItemRepository
import com.kkw.pawket.point.domain.repository.PointShopRepository
import com.kkw.pawket.point.model.req.CreatePointShopItemReq
import com.kkw.pawket.point.model.req.CreatePointShopReq
import com.kkw.pawket.point.service.PointShopService
import com.kkw.pawket.user.domain.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PointShopServiceUnitTest {

    private val userRepository = mockk<UserRepository>()
    private val pointShopRepository = mockk<PointShopRepository>()
    private val pointShopItemRepository = mockk<PointShopItemRepository>()

    private lateinit var pointShopService: PointShopService

    @BeforeEach
    fun setUp() {
        pointShopService = PointShopService(userRepository, pointShopRepository, pointShopItemRepository)
        clearAllMocks()
    }

    // createPointShop 테스트
    @Test
    fun `유저가 존재하지 않을 때 포인트샵 생성 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointShopReq(name = "테스트샵")

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns false

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShop(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
    }

    @Test
    fun `포인트샵 이름 중복시 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointShopReq(name = "중복샵")

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopRepository.existsByNameAndIsDeletedFalse(req.name) } returns true

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShop(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.POINT_SHOP_NAME_DUPLICATED
    }

    @Test
    fun `포인트샵 정상 생성`() {
        // given
        val userId = "user1"
        val req = CreatePointShopReq(name = "새로운샵")
        val pointShopId = "shop1"
        val pointShop = mockk<PointShop>()

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopRepository.existsByNameAndIsDeletedFalse(req.name) } returns false

        mockkObject(PointShop.Companion)
        every { PointShop.create(req.name) } returns pointShop
        every { pointShop.id } returns pointShopId
        every { pointShopRepository.save(any()) } returns pointShop

        // when
        val result = pointShopService.createPointShop(userId, req)

        // then
        result shouldBe pointShopId
        verify { pointShopRepository.save(any()) }
    }

    // createPointShopItem 테스트
    @Test
    fun `유저가 존재하지 않을 때 포인트샵 아이템 생성 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointShopItemReq(pointShopId = "shop1", name = "아이템1", price = 1000)

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns false

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
    }

    @Test
    fun `포인트샵 아이템 이름 중복시 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointShopItemReq(pointShopId = "shop1", name = "중복아이템", price = 1000)

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.pointShopId) } returns false

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.POINT_SHOP_ITEM_NAME_DUPLICATED
    }

    @Test
    fun `포인트샵이 존재하지 않을 때 아이템 생성 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointShopItemReq(pointShopId = "shop1", name = "아이템1", price = 1000)

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.pointShopId) } returns true
        every { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) } returns null

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.POINT_SHOP_NOT_FOUND
    }

    @Test
    fun `포인트샵 아이템 정상 생성`() {
        // given
        val userId = "user1"
        val req = CreatePointShopItemReq(pointShopId = "shop1", name = "새아이템", price = 1500)
        val pointShop = mockk<PointShop>()
        val pointShopItemId = "item1"
        val pointShopItem = mockk<PointShopItem>()

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.pointShopId) } returns true
        every { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) } returns pointShop

        mockkObject(PointShopItem.Companion)
        every { PointShopItem.create(pointShop, req.name, req.price) } returns pointShopItem
        every { pointShopItem.id } returns pointShopItemId
        every { pointShopItemRepository.save(any()) } returns pointShopItem

        // when
        val result = pointShopService.createPointShopItem(userId, req)

        // then
        result shouldBe pointShopItemId
        verify { pointShopItemRepository.save(any()) }
    }
}