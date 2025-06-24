package com.kkw.pawket.ai.model.req

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// Pet Chat - Request
data class PetChatReq(
    @field:NotBlank(message = "메시지는 필수입니다")
    @field:Size(max = 1000, message = "메시지는 1000자를 초과할 수 없습니다")
    val message: String,

    val petId: String,

    val sessionId: String? = null,


)