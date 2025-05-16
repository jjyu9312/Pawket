package com.kkw.pawket.partner.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreatePartnerReq
import com.kkw.pawket.partner.model.res.PartnerRegistrationStatusRes
import com.kkw.pawket.partner.service.PartnerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/partner")
@Tag(name = "Partner", description = "제휴업체 관련 API")
class PartnerController(
    private val partnerService: PartnerService,
) {

    @Operation(
        summary = "제휴업체 등록",
        description = "User 로그인 후 제휴업체 선택 시 필요한 추가 정보를 등록합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "제휴업체 등록 성공",
                content = [Content(schema = Schema(implementation = String::class))]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            )
        ]
    )
    @PostMapping
    fun createPartner(
        @AuthenticationPrincipal userId: String,
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
    TODO 제휴업체로 가입되어 있는지 아닌지 조회 후 가입되지 않으면 false return 하고 가입되어 있으면 가입된 정보 return
     */
    @GetMapping("/registration/check")
    fun checkPartnerRegistration(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<ApiResponse<PartnerRegistrationStatusRes>> {
        return try {
            val result = partnerService.checkPartnerRegistration(userId)
            ApiResponseFactory.success(result)
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

    /*
    TODO 제휴업체 정보 수정
     */

    /*
    TODO 내 위치 근처 제휴업체 조회
     */
}