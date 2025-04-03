package com.kkw.pawket.partner.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreatePartnerReq
import com.kkw.pawket.partner.service.PartnerService
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/partner")
class PartnerController(
    private val partnerService: PartnerService,
) {
    /*
    TODO 제휴업체 등록
     */
    @PostMapping("/user/{userId}")
    fun createPartner(
        @PathVariable userId: String,
        @RequestBody req: CreatePartnerReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val partnerId = partnerService.createPartner(userId, req)
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
    TODO 제휴업체 정보 수정
     */

    /*
    TODO 내 위치 근처 제휴업체 조회
     */
}