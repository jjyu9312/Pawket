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
        // clearAllMocks() 제거 - 이것이 주요 문제였음
        clearMocks(userRepository, pointShopRepository, pointShopItemRepository)
        pointShopService = PointShopService(userRepository, pointShopRepository, pointShopItemRepository)
    }

    @Test
    fun `유저가 존재하지 않을 때 포인트샵 아이템 생성 예외 발생`() {
        val userId = "user1"
        val req = CreatePointShopItemReq("shop1", "아이템1", 1000)

        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns false
        // 유저가 존재하지 않으므로 다른 repository 호출은 발생하지 않음

        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND

        // 유저 체크 이후의 메서드들은 호출되지 않았는지 검증
        verify { userRepository.existsByIdAndIsDeletedFalse(userId) }
        verify(exactly = 0) { pointShopItemRepository.existsByNameAndIsDeletedFalse(any()) }
    }

    @Test
    fun `포인트샵 아이템 이름 중복시 예외 발생`() {
        val userId = "user1"
        val req = CreatePointShopItemReq("shop1", "중복아이템", 1000)

        // 서비스 로직 순서에 맞게 모든 필요한 mock 설정
        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) } returns true

        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.POINT_SHOP_ITEM_DUPLICATED

        // 호출 순서 검증
        verify { userRepository.existsByIdAndIsDeletedFalse(userId) }
        verify { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) }
        verify(exactly = 0) { pointShopRepository.findByIdAndIsDeletedFalse(any()) }
    }

    @Test
    fun `포인트샵이 존재하지 않을 때 아이템 생성 예외 발생`() {
        val userId = "user1"
        val req = CreatePointShopItemReq("shop1", "아이템1", 1000)

        // 서비스 로직 순서에 맞게 모든 필요한 mock 설정
        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) } returns false
        every { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) } returns null

        val exception = shouldThrow<BadRequestException> {
            pointShopService.createPointShopItem(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.POINT_SHOP_NOT_FOUND

        // 호출 순서 검증
        verify { userRepository.existsByIdAndIsDeletedFalse(userId) }
        verify { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) }
        verify { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) }
    }

    @Test
    fun `포인트샵 아이템 정상 생성`() {
        val userId = "user1"
        val req = CreatePointShopItemReq("shop1", "새아이템", 1500)
        val pointShop = mockk<PointShop>()
        val pointShopItem = mockk<PointShopItem>()
        val pointShopItemId = "item1"

        // 서비스 로직 순서에 맞게 모든 필요한 mock 설정
        every { userRepository.existsByIdAndIsDeletedFalse(userId) } returns true
        every { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) } returns false
        every { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) } returns pointShop

        // PointShopItem의 companion object mock
        mockkObject(PointShopItem.Companion)
        every { PointShopItem.create(pointShop, req.name, req.price) } returns pointShopItem
        every { pointShopItem.id } returns pointShopItemId
        every { pointShopItemRepository.save(pointShopItem) } returns pointShopItem

        val result = pointShopService.createPointShopItem(userId, req)

        result shouldBe pointShopItemId

        // 모든 메서드가 올바른 순서로 호출되었는지 검증
        verify { userRepository.existsByIdAndIsDeletedFalse(userId) }
        verify { pointShopItemRepository.existsByNameAndIsDeletedFalse(req.name) }
        verify { pointShopRepository.findByIdAndIsDeletedFalse(req.pointShopId) }
        verify { PointShopItem.create(pointShop, req.name, req.price) }
        verify { pointShopItemRepository.save(pointShopItem) }

        // companion object mock 해제
        unmockkObject(PointShopItem.Companion)
    }
}