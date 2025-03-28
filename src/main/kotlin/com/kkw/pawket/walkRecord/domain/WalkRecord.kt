package com.kkw.pawket.walkRecord.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
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

    @Column(nullable = true)
    val endDateTime: LocalDateTime? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val walkLocation: String? = null,

): BaseEntity() {
    companion object {
        private val objectMapper = jacksonObjectMapper()

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
        walkLocation = createWalkLocationJson(distance, coordinateLat, coordinateLng),
    )
}