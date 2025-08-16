package com.kkw.pawket.walkRecord.model.req

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class CreateWalkRecordReq(
    @Schema(description = "Database id of the pet", example = "petId")
    val dogId: String,
)
