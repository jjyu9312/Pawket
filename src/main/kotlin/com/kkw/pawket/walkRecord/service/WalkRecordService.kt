package com.kkw.pawket.walkRecord.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.walkRecord.domain.WalkRecord
import com.kkw.pawket.walkRecord.domain.repository.WalkRecordRepository
import com.kkw.pawket.walkRecord.model.req.CreateWalkRecordReq
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class WalkRecordService (
    private val walkRecordRepository: WalkRecordRepository,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(WalkRecordService::class.java)
    private val objectMapper = jacksonObjectMapper()


    fun createWalkRecord(userId: String, req: CreateWalkRecordReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(
                ResponseCode.USER_NOT_FOUND.defaultMessage
            )

        val walkRecord = WalkRecord.create(
            user = user,
            petId = req.petId,
            startedAt = req.startedAt,
        )

        walkRecordRepository.save(walkRecord)

        return walkRecord.id
    }

    // walk record 상세 정보를 JSON으로 변환
    fun createWalkLocationJson(
        distance: String,
        coordinateLat: String,
        coordinateLng: String,
    ): String {
        val locationMap = mapOf(
            "distance" to distance,
            "coordinateLat" to coordinateLat,
            "coordinateLng" to coordinateLng,
        )
        return objectMapper.writeValueAsString(locationMap)
    }
}