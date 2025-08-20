package com.kkw.pawket.ai.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.ai.model.req.DogChatReq
import com.kkw.pawket.ai.model.res.*
import com.kkw.pawket.ai.service.ClovaAgentService
import com.kkw.pawket.ai.service.ChatSessionService
import com.kkw.pawket.dog.service.DogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Dog Agent API", description = "반려동물 AI 상담 API")
@RestController
@RequestMapping("/api/v1/dog-agent")
class DogAgentController(
    private val clovaAgentService: ClovaAgentService,
    private val chatSessionService: ChatSessionService,
    private val dogService: DogService,
) {
    private val logger = LoggerFactory.getLogger(DogAgentController::class.java)

    /*
    TODO AI 채팅 상담
     */
    @Operation(
        summary = "AI 채팅 상담",
        description = "내 반려동물 관련 질문에 대해 AI 상담을 받습니다."
    )
    @PostMapping("/chat")
    suspend fun chat(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestBody req: DogChatReq
    ): ResponseEntity<ApiResponse<DogChatRes>> {
        return try {
            // 세션 컨텍스트 조회
            val context = req.sessionId?.let {
                chatSessionService.getContext(it)
            } ?: emptyList()

            logger.info("User $userId is chatting with Dog agent. Session ID: ${req.sessionId}, Message: ${req.message}")
            logger.info("Context for session ${req.sessionId}: $context")

            // 사용자의 반려동물 프로필 조회
            val DogProfileRes = dogService.findDogProfileByUserAndDogId(
                userId = userId,
                dogId = req.dogId
            )

            // CLOVA 응답 생성
            val (reply, tokenUsage) = clovaAgentService.generateResponse(
                userMessage = req.message,
                context = context,
                dogProfile = DogProfileRes.dogProfile
            )

            // 세션 업데이트
            val sessionId = req.sessionId ?: chatSessionService.createNewSession(userId)
            chatSessionService.updateSession(
                sessionId = sessionId,
                userMessage = req.message,
                assistantReply = reply
            )

            val response = DogChatRes(
                reply = reply,
                sessionId = sessionId,
                tokenUsage = tokenUsage
            )

            ApiResponseFactory.success(response)

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 채팅 히스토리 조회
     */
    @Operation(
        summary = "채팅 히스토리 조회",
        description = "세션의 채팅 히스토리를 조회합니다."
    )
    @GetMapping("/chat/{sessionId}/history")
    fun getChatHistory(
        @AuthenticationPrincipal userId: String,
        @PathVariable sessionId: String
    ): ResponseEntity<ApiResponse<ChatHistoryRes>> {
        return try {
            val history = chatSessionService.getContextWithValidation(userId, sessionId)
            val response = ChatHistoryRes(
                sessionId = sessionId,
                messages = history.map {
                    ChatMessageRes(
                        role = it.role,
                        content = it.content
                    )
                },
                totalCount = history.size
            )
            ApiResponseFactory.success(response)

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 채팅 세션 삭제
     */
    @Operation(
        summary = "채팅 세션 삭제",
        description = "채팅 세션을 삭제합니다."
    )
    @DeleteMapping("/chat/{sessionId}")
    fun clearChatHistory(
        @AuthenticationPrincipal userId: String,
        @PathVariable sessionId: String
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            chatSessionService.clearSessionWithValidation(userId, sessionId)
            ApiResponseFactory.success("채팅 세션이 삭제되었습니다.")

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 반려동물 프로필 조회
     */
    @Operation(
        summary = "반려동물 프로필 조회",
        description = "반려동물 프로필 정보를 조회합니다."
    )
    @GetMapping("/profile/{DogId}")
    fun getDogProfile(
        @AuthenticationPrincipal userId: String,
        @PathVariable DogId: String
    ): ResponseEntity<ApiResponse<DogProfileRes>> {
        return try {
            val DogProfileRes = dogService.findDogProfileByUserAndDogId(userId, DogId)

            ApiResponseFactory.success(DogProfileRes)

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 사용자의 모든 반려동물 프로필 목록 조회
     */
    @Operation(
        summary = "반려동물 프로필 목록 조회",
        description = "사용자의 모든 반려동물 프로필 목록을 조회합니다."
    )
    @GetMapping("/profiles")
    fun getDogProfiles(
        @AuthenticationPrincipal userId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<List<DogProfileRes>>> {
        return try {
            val profiles = dogService.findDogProfilesByUserId(userId, page, size)
            ApiResponseFactory.success(profiles)

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 사용자의 활성 채팅 세션 목록 조회
     */
    @Operation(
        summary = "활성 채팅 세션 목록 조회",
        description = "사용자의 활성 채팅 세션 목록을 조회합니다."
    )
    @GetMapping("/chat/sessions")
    fun getActiveSessions(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<ApiResponse<List<String>>> {
        return try {
            val sessions = chatSessionService.getActiveSessionsByUserId(userId)
            ApiResponseFactory.success(sessions)

        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    /*
    TODO 서비스 상태 확인
     */
    @Operation(
        summary = "서비스 상태 확인",
        description = "Dog Agent 서비스의 상태를 확인합니다."
    )
    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<ApiResponse<Map<String, Any>>> {
        return try {
            val healthInfo = mapOf(
                "status" to "UP",
                "service" to "Dog Agent API",
                "version" to "1.0.0",
                "timestamp" to System.currentTimeMillis()
            )
            ApiResponseFactory.success(healthInfo)

        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }
}