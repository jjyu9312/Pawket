package com.kkw.pawket.walkRecord.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.walkRecord.model.req.CompleteWalkReq
import com.kkw.pawket.walkRecord.model.req.CreateWalkRecordReq
import com.kkw.pawket.walkRecord.service.WalkRecordService
import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/walk-record")
class WalkRecordController (
    private val walkRecordService: WalkRecordService,
) {
    /*
    TODO 산책 기록 등록
     */
    @PostMapping("/user/{userId}")
    fun createWalkRecord(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreateWalkRecordReq,
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val walkRecord = walkRecordService.createWalkRecord(userId, req)
            ApiResponseFactory.success(walkRecord)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,  // 400 응답 코드
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,  // 500 응답 코드
                customMessage = e.message
            )
        }
    }

    /*
    TODO 산책 종료 시 위치 정보 등록
     */
    @PutMapping("/{walkRecordId}/location")
    fun completeWalk(
        @PathVariable walkRecordId: String,
        @RequestBody req: CompleteWalkReq,
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val walkRecord = walkRecordService.completeWalk(walkRecordId, req)
            ApiResponseFactory.success(walkRecord)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,  // 400 응답 코드
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,  // 500 응답 코드
                customMessage = e.message
            )
        }
    }

    /*
    TODO 산책 기록 수정
     */

    /*
    TODO 산책 기록 조회
     */

    /*
    TODO userId로 산책 기록 조회
     */
}