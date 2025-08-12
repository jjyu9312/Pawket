package com.kkw.pawket.ai.model.res

import com.fasterxml.jackson.annotation.JsonFormat
import com.kkw.pawket.ai.model.common.DogProfile
import java.time.LocalDateTime

// Pet Profile - Response
data class DogProfileRes(
    val dogProfile: DogProfile,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssxxxxx")
    val createdAt: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssxxxxx")
    val updatedAt: LocalDateTime
)