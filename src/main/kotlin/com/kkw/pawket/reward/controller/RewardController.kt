package com.kkw.pawket.reward.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.reward.model.req.CreateRewardReq
import com.kkw.pawket.reward.service.RewardService
import io.swagger.v3.oas.annotations.Operation
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reward")
class RewardController(
    private val rewardService: RewardService,
) {
    /*
    TODO 보상 등록
     */
    @Operation(
        summary = "보상 등록",
        description = "보상을 등록합니다."
    )
    @PostMapping
    fun createReward(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreateRewardReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val partnerId = rewardService.createReward(userId, req)
            ApiResponseFactory.success(partnerId)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }


}