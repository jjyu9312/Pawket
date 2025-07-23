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

class PointServiceUnitTest {

    private val userRepository = mockk<UserRepository>()
    private val pointRepository = mockk<PointRepository>()
    private val userPointHistoryRepository = mockk<UserPointHistoryRepository>()

    private lateinit var pointService: PointService

    @BeforeEach
    fun setUp() {
        pointService = PointService(userRepository, pointRepository, userPointHistoryRepository)

        // 💡 공통 Companion mock 선언을 한 번만
        mockkObject(PointType.Companion)
        mockkObject(Point.Companion)
        mockkObject(UserPointHistory.Companion)

        clearAllMocks()
    }

    @Test
    fun `WALK 포인트 정상 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "walk", pointHistoryType = "collect", point = 100)
        val pointId = "point1"
        val point = mockk<Point>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        every { PointType.fromString("walk") } returns PointType.WALK
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.WALK) } returns 2L

        every { Point.create(100, PointType.WALK) } returns point
        every { point.id } returns pointId
        every { pointRepository.save(point) } returns point // ✅ 중요: save 반환값 설정

        val userPointHistory = mockk<UserPointHistory>()
        every { UserPointHistory.createByCollectPoint(user, point) } returns userPointHistory
        every { userPointHistoryRepository.save(userPointHistory) } returns userPointHistory

        // when
        val result = pointService.createPoint(userId, req)

        // then
        result shouldBe pointId
        verify { pointRepository.save(point) }
        verify { userPointHistoryRepository.save(userPointHistory) }
    }

    @Test
    fun `AD 포인트 정상 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        val req = CreatePointReq(type = "ad", pointHistoryType = "collect", point = 50)
        val pointId = "point2"
        val point = mockk<Point>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        every { PointType.fromString("ad") } returns PointType.AD
        every { userPointHistoryRepository.countWalkPointHistoryByUserAndType(user, PointType.AD) } returns 1L

        every { Point.create(50, PointType.AD) } returns point
        every { point.id } returns pointId
        every { pointRepository.save(point) } returns point // ✅ 중요: save 반환값 설정

        val userPointHistory = mockk<UserPointHistory>()
        every { UserPointHistory.createByCollectPoint(user, point) } returns userPointHistory
        every { userPointHistoryRepository.save(userPointHistory) } returns userPointHistory

        // when
        val result = pointService.createPoint(userId, req)

        // then
        result shouldBe pointId
        verify { pointRepository.save(point) }
        verify { userPointHistoryRepository.save(userPointHistory) }
    }
}
