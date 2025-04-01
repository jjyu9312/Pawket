package com.kkw.pawket.walkRecord.model.req

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CreateWalkRecordReq(
    val petId: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startedAt: LocalDateTime,
)
