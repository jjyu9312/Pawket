package com.kkw.petwalker.common.response

import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler { // 전역 예외 처리기를 통해 예상치 못한 오류를 처리하고, 상태 코드를 적절히 반환

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred: ${ex.localizedMessage}"
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage ?: "Invalid argument"
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage ?: "Bad request"
        )
    }
}
