package com.kkw.pawket.walkRecord.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.walkRecord.domain.WalkRecord
import com.kkw.pawket.walkRecord.domain.repository.WalkRecordRepository
import com.kkw.pawket.walkRecord.model.req.CompleteWalkReq
import com.kkw.pawket.walkRecord.model.req.CreateWalkRecordReq
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WalkRecordService (
    private val walkRecordRepository: WalkRecordRepository,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    fun createWalkRecord(userId: String, req: CreateWalkRecordReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val walkRecord = WalkRecord.create(
            user = user,
            petId = req.petId,
        )

        logger.info("WalkRecord created: $walkRecord")

        walkRecordRepository.save(walkRecord)

        return walkRecord.id
    }

    // walk record 상세 정보를 JSON으로 변환
    fun createWalkLocationJson(
        distance: Int, // m 기반
        lat: Double,
        lng: Double,
    ): String {
        val locationMap = mapOf(
            "distance" to distance,
            "lat" to lat,
            "lng" to lng,
        )
        return objectMapper.writeValueAsString(locationMap)
    }

    fun completeWalk(walkRecordId: String, req: CompleteWalkReq): String? {
        val walkRecord = walkRecordRepository.findByIdAndIsDeletedFalse(walkRecordId)
            ?: throw BadRequestException(ResponseCode.WALK_RECORD_NOT_FOUND)

        val distance = req.distance
        val coordinateLat = req.coordinateLat
        val coordinateLng = req.coordinateLng

        val walkLocationJson = createWalkLocationJson(distance, coordinateLat, coordinateLng)

        logger.info("WalkRecord completed: $walkRecord, WalkLocation: $walkLocationJson")

        walkRecord.update(
            finishedAt = req.finishedAt,
            walkLocation = walkLocationJson,
        )

        return walkRecord.id
    }
}