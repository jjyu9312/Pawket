package com.kkw.petwalker.spot.controller

import com.kkw.petwalker.common.response.ApiResponse
import com.kkw.petwalker.common.response.ApiResponseFactory
import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.spot.model.req.CreateSpotReq
import com.kkw.petwalker.spot.service.SpotService
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/spot")
class SpotController(private val spotService: SpotService) {
    @PostMapping
    fun createSpot(
        @RequestParam companyId: String,
        @RequestBody req: CreateSpotReq,
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val spotId = spotService.createSpot(companyId, req)
            ApiResponseFactory.success(spotId)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,  // 400 응답 코드
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,  // 500 응답 코드
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }


}