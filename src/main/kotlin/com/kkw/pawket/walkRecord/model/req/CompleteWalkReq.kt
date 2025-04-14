package com.kkw.pawket.walkRecord.model.req

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class CompleteWalkReq(
    @Schema(description = "산책 거리", example = "1000")
    val distance: Int,

    @Schema(description = "산책 종료 경로 위도", example = "37.123456")
    val lat: Double,

    @Schema(description = "산책 종료 경로 경도", example = "127.123456")
    val lng: Double,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val finishedAt: LocalDateTime,
)
