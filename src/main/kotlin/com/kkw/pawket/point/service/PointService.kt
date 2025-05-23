package com.kkw.pawket.point.service

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.point.domain.*
import com.kkw.pawket.point.domain.repository.PointRepository
import com.kkw.pawket.point.domain.repository.UserPointHistoryRepository
import com.kkw.pawket.point.model.req.CreatePointReq
import com.kkw.pawket.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userRepository: UserRepository,
    private val pointRepository: PointRepository,
    private val userPointHistoryRepository: UserPointHistoryRepository,
) {
    fun createPoint(userId: String, req: CreatePointReq): String? {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val pointType = PointType.fromString(req.type)
            ?: throw BadRequestException(ResponseCode.INVALID_POINT_TYPE)

        // 포인트 제약조건
        // 1. type이 walk일 때는 4번까지만 가능

        val historyCount = userPointHistoryRepository
            .countWalkPointHistoryByUserAndType(user, pointType)

        when (pointType) {
            PointType.WALK -> {
                if (historyCount > 4L) {
                    throw BadRequestException(ResponseCode.WALK_POINT_LIMIT_EXCEEDED)
                }
            }
            PointType.AD -> {
                if (historyCount > 2L) {
                    throw BadRequestException(ResponseCode.ADS_POINT_LIMIT_EXCEEDED)
                }
            }
            // 추가 분기
            else -> {}
        }

        val point = Point.create(
            type = pointType,
            point = req.point,
        )
        pointRepository.save(point)

        // TODO 포인트 이력 저장
        val userPointHistory = UserPointHistory.create(
            user = user,
            point = point,
            beforePoint = 0,
            afterPoint = point.point,
        )
        userPointHistoryRepository.save(userPointHistory)

        return point.id
    }
}