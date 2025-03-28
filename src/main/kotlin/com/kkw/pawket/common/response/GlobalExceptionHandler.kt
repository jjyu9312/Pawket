package com.kkw.pawket.common.response

import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler { // 전역 예외 처리기를 통해 예상치 못한 오류를 처리하고, 상태 코드를 적절히 반환

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(ResponseCode.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(ResponseCode.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(ResponseCode.BAD_REQUEST)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponseFactory.error(ResponseCode.BAD_REQUEST)
    }
}
