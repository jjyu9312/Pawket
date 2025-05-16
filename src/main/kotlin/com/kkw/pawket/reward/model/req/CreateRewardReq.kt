package com.kkw.pawket.reward.model.req

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

data class CreateRewardReq(
    val coin: Int,
)
