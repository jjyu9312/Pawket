package com.kkw.pawket.ai.model.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// Pet Chat - Request
data class PetChatReq(
    @field:NotBlank(message = "메시지는 필수입니다")
    @field:Size(max = 1000, message = "메시지는 1000자를 초과할 수 없습니다")
    @Schema(
        description = "사용자가 입력한 메시지",
        example = "안녕, 내 강아지 이름은 뭐였지?"
    )
    val message: String,

    @field:NotBlank(message = "반려동물 ID는 필수입니다")
    @Schema(
        description = "반려동물 ID",
        example = "pet-12345"
    )
    val petId: String,

    @field:NotBlank(message = "사용자 ID는 필수입니다")
    @Schema(
        description = "세션 ID (선택 사항, 대화 세션을 식별하는 데 사용)",
        example = "session-67890"
    )
    val sessionId: String? = null,
)