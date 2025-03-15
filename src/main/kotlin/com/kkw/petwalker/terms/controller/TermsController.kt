package com.kkw.petwalker.terms.controller

import com.kkw.petwalker.common.response.ApiResponse
import com.kkw.petwalker.common.response.ApiResponseFactory
import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.terms.model.req.TermsCreateReq
import com.kkw.petwalker.terms.model.res.RequiredTermsAgreeCheckRes
import com.kkw.petwalker.terms.model.res.TermsListRes
import com.kkw.petwalker.terms.service.TermsService
import io.swagger.v3.oas.annotations.Operation
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/terms")
class TermsController (
    private val termsService: TermsService
) {
    @Operation(summary = "약관 생성", description = "요청에 맞게 약관을 생성합니다.")
    @PostMapping
    fun createTerms(
        @RequestBody req: TermsCreateReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val termsId = termsService.createTerms(req)
            ApiResponseFactory.success(termsId)
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

    @Operation(summary = "약관 목록 조회", description = "약관 목록을 조회합니다.")
    @GetMapping
    fun getTerms(): ResponseEntity<ApiResponse<List<TermsListRes>>> {
        return try {
            val termsList = termsService.getTerms()
            ApiResponseFactory.success(termsList)
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

    @Operation(summary = "약관 동의", description = "약관을 동의합니다.")
    @PostMapping("/agree")
    fun agreeToTerms(
        @RequestParam userId: String,
        @RequestBody termsList: List<String>
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            termsService.agreeToTerms(userId, termsList)
            ApiResponseFactory.success("약관 동의가 완료되었습니다.")
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

    @Operation(summary = "유저의 필수 약관 동의 여부 체크", description = "유저가 필수 약관 동의를 했는지 확인합니다.")
    @GetMapping("/agreed-check")
    fun checkAgreedTerms(
        @RequestParam userId: String,
    ): ResponseEntity<ApiResponse<RequiredTermsAgreeCheckRes>> {
        return try {
            val result = termsService.checkAgreedTerms(userId)
            ApiResponseFactory.success(result)
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