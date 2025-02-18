package com.kkw.petwalker.walkRecord.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class WalkRecord(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(columnDefinition = "CHAR(36)")
    val petId: String,

    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime,

    @Column(nullable = false, columnDefinition = "TEXT") // JSON 데이터 저장
    val walkDetail: String,

): BaseEntity() {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        // walk record 상세 정보를 JSON으로 변환
        fun createWalkDetailJson(
            distance: String,
            coordinateLat: String,
            coordinateLng: String,
        ): String {
            val detailMap = mapOf(
                "distance" to distance,
                "coordinateLat" to coordinateLat,
                "coordinateLng" to coordinateLng,
            )
            return objectMapper.writeValueAsString(detailMap)
        }
    }

    constructor(
        user: User,
        petId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        distance: String,
        coordinateLat: String,
        coordinateLng: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        petId = petId,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        walkDetail = createWalkDetailJson(distance, coordinateLat, coordinateLng),
    )
}