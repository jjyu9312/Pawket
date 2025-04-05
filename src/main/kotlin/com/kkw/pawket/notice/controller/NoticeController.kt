package com.kkw.pawket.notice.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.notice.domain.Notice
import com.kkw.pawket.notice.domain.repository.NoticeRepository
import com.kkw.pawket.notice.model.req.CreateNoticeReq
import com.kkw.pawket.notice.service.NoticeService
import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {
    /*
    TODO 공지사항 등록
     */
    @PostMapping("")
    fun createNotice(
        @RequestBody req: CreateNoticeReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val noticeId = noticeService.createNotice(req)
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

    /*
    TODO 공지사항 정보 조회
     */

    /*
    TODO 회원 유형으로 공지사항 정보 조회
     */
}