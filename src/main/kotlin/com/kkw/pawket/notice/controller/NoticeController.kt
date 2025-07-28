package com.kkw.pawket.notice.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.notice.domain.Notice
import com.kkw.pawket.notice.domain.repository.NoticeRepository
import com.kkw.pawket.notice.model.req.CreateNoticeReq
import com.kkw.pawket.notice.model.res.NoticeDetailRes
import com.kkw.pawket.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notices")
class NoticeController(
    private val noticeService: NoticeService,
) {
    /*
    TODO 공지사항 등록
     */
    @Operation(
        summary = "공지사항 등록",
        description = "공지사항을 등록합니다."
    )
    @PostMapping("")
    fun createNotice(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreateNoticeReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val noticeId = noticeService.createNotice(userId, req)
            ApiResponseFactory.success(noticeId)
        }  catch (e: BadRequestException) {
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
    TODO 공지사항 정보 수정
     */
    @Operation(
        summary = "공지사항 수정",
        description = "공지사항 정보를 수정합니다."
    )
    @PutMapping("/{noticeId}")
    fun updateNotice(
        @AuthenticationPrincipal userId: String,
        @PathVariable noticeId: String,
        @RequestBody req: CreateNoticeReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val updateNoticeId = noticeService.updateNotice(userId, noticeId, req)
            ApiResponseFactory.success(updateNoticeId)
        }  catch (e: BadRequestException) {
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
    TODO 공지사항 정보 삭제
     */
    @Operation(
        summary = "공지사항 삭제",
        description = "공지사항 정보를 삭제합니다."
    )
    @DeleteMapping("/{noticeId}")
    fun deleteNotice(
        @AuthenticationPrincipal userId: String,
        @PathVariable noticeId: String
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            noticeService.deleteNotice(userId, noticeId)
            ApiResponseFactory.success("공지사항 삭제가 완료되었습니다.")
        }  catch (e: BadRequestException) {
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
    TODO 공지사항 ID로 조회
     */
    @Operation(
        summary = "공지사항 정보 조회",
        description = "공지사항 정보를 조회합니다."
    )
    @GetMapping("/{noticeId}")
    fun getNotice(
        @AuthenticationPrincipal userId: String,
        @PathVariable noticeId: String
    ): ResponseEntity<ApiResponse<NoticeDetailRes>> {
        return try {
            val notice = noticeService.findNoticeById(userId, noticeId)
            ApiResponseFactory.success(notice)
        }  catch (e: BadRequestException) {
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
    TODO 회원 유형으로 공지사항 정보 조회
     */
}