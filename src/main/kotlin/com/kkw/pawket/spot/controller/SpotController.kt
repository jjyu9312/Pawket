package com.kkw.pawket.spot.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.spot.model.req.CreateSpotReq
import com.kkw.pawket.spot.service.SpotService
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/spots")
class SpotController(private val spotService: SpotService) {
    /*
    TODO 스팟 생성
     */
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

    /*
    TODO 스팟 정보 수정
     */

    /*
    TODO 스팟 정보 조회
     */

    /*
    TODO companyId로 스팟 정보 조회
     */

    /*
    TODO spotId로 리워드 조회
     */

}