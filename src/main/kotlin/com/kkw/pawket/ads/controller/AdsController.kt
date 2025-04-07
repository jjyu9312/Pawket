package com.kkw.pawket.ads.controller

import com.kkw.pawket.ads.service.AdsService
import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreateCompanyReq
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/ads")
class AdsController(
    private val adsService: AdsService
) {
    /*
    TODO Ads Company 생성
     */
    @PostMapping("/company/user/{userId}")
    fun createPartner(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreateCompanyReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val partnerId = adsService.createCompany(userId, req)
            ApiResponseFactory.success(partnerId)
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
    TODO Ads Company 조회
     */

    /*
    TODO Ads Company 수정
     */

    /*
    TODO Ads Company 삭제
     */
}