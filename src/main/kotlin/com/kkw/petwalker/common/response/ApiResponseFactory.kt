package com.kkw.petwalker.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ApiResponseFactory {
    fun <T> success(
        data: T?,
        responseCode: ResponseCode = ResponseCode.SUCCESS // 프로젝트에서 사용할 code 정리
    ): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity(
            ApiResponse(
                code = responseCode.code,
                message = responseCode.defaultMessage,
                data = data
            ),
            HttpStatus.OK
        )
    }

    fun <T> error(
        responseCode: ResponseCode,
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
        customMessage: String? = null  // 커스텀 메시지 파라미터
    ): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity(
            ApiResponse(
                code = responseCode.code,
                message = responseCode.withCustomMessage(customMessage),  // 커스텀 메시지와 기본 메시지 조합
                data = null
            ),
            httpStatus
        )
    }
}
