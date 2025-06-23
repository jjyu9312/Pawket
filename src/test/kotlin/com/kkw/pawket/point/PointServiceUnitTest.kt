package com.kkw.pawket.point

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.point.domain.*
import com.kkw.pawket.point.domain.repository.PointRepository
import com.kkw.pawket.point.domain.repository.UserPointHistoryRepository
import com.kkw.pawket.point.model.req.CreatePointReq
import com.kkw.pawket.point.service.PointService
import com.kkw.pawket.user.domain.User
import com.kkw.pawket.user.domain.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/*
 * 포인트샵 유닛 테스트
 *
 */
class PointServiceUnitTest {

    private val userRepository = mockk<UserRepository>()
    private val pointRepository = mockk<PointRepository>()
    private val userPointHistoryRepository = mockk<UserPointHistoryRepository>()

    private lateinit var pointService: PointService

    @BeforeEach
    fun setUp() {
        pointService = PointService(userRepository, pointRepository, userPointHistoryRepository)
        clearAllMocks()
    }

    @Test
    fun `유저가 존재하지 않을 때 예외 발생`() {
        // given
        val userId = "user1"
        val req = CreatePointReq(type = "walk", point = 100)

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns null

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointService.createPoint(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
    }

    @Test
    fun `잘못된 포인트 타입일 때 예외 발생`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "invalid", point = 100)

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        mockkObject(PointType.Companion)
        every { PointType.fromString("invalid") } returns null

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointService.createPoint(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.INVALID_POINT_TYPE
    }

    @Test
    fun `WALK 포인트 한도 초과시 예외 발생`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "walk", point = 100)

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        mockkObject(PointType.Companion)
        every { PointType.fromString("walk") } returns PointType.WALK
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.WALK) } returns 5L

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointService.createPoint(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.WALK_POINT_LIMIT_EXCEEDED
    }

    @Test
    fun `AD 포인트 한도 초과시 예외 발생`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "ad", point = 50)

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        mockkObject(PointType.Companion)
        every { PointType.fromString("ad") } returns PointType.AD
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.AD) } returns 3L

        // when & then
        val exception = shouldThrow<BadRequestException> {
            pointService.createPoint(userId, req)
        }
        exception.responseCode shouldBe ResponseCode.ADS_POINT_LIMIT_EXCEEDED
    }

    @Test
    fun `WALK 포인트 정상 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "walk", point = 100)
        val pointId = "point1"
        val point = mockk<Point>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        mockkObject(PointType.Companion)
        every { PointType.fromString("walk") } returns PointType.WALK
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.WALK) } returns 2L

        mockkObject(Point.Companion)
        every { Point.create(500, PointType.WALK) } returns point
        every { point.id } returns pointId
        every { point.petPoint } returns 100
        every { pointRepository.save(any()) } returns point

        mockkObject(UserPointHistory.Companion)
        val userPointHistory = mockk<UserPointHistory>()
        every { UserPointHistory.create(user, point, 0, 100) } returns userPointHistory
        every { userPointHistoryRepository.save(userPointHistory) } returns userPointHistory

        // when
        val result = pointService.createPoint(userId, req)

        // then
        result shouldBe pointId
        verify { pointRepository.save(any()) }
        verify { userPointHistoryRepository.save(any()) }
    }

    @Test
    fun `AD 포인트 정상 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "ad", point = 50)
        val pointId = "point2"
        val point = mockk<Point>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        mockkObject(PointType.Companion)
        every { PointType.fromString("ad") } returns PointType.AD
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.AD) } returns 1L

        mockkObject(Point.Companion)
        every { Point.create(50, PointType.AD) } returns point
        every { point.id } returns pointId
        every { point.petPoint } returns 50
        every { pointRepository.save(point) } returns point

        mockkObject(UserPointHistory.Companion)
        val userPointHistory = mockk<UserPointHistory>()
        every { UserPointHistory.create(user, point, 0, 50) } returns userPointHistory
        every { userPointHistoryRepository.save(userPointHistory) } returns userPointHistory

        // when
        val result = pointService.createPoint(userId, req)

        // then
        result shouldBe pointId
        verify { pointRepository.save(any()) }
        verify { userPointHistoryRepository.save(any()) }
    }
}