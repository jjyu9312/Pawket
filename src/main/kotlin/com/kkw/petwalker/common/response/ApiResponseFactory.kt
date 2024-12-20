package com.kkw.petwalker.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ApiResponseFactory { // ResponseEntity를 포함한 반환 구조를 추가하여 API 응답을 생성
    fun <T> success(data: T?, message: String = "success"): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity(
            ApiResponse(
                statusCode = HttpStatus.OK.value(),
                message = message,
                data = data
            ),
            HttpStatus.OK
        )
    }

    fun <T> error(statusCode: HttpStatus, message: String): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity(
            ApiResponse(
                statusCode = statusCode.value(),
                message = message,
                data = null
            ),
            statusCode
        )
    }
}

