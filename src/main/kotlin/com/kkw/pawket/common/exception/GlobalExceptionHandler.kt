package com.kkw.pawket.common.exception

import com.kkw.pawket.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler { // 전역 예외 처리기를 통해 예상치 못한 오류를 처리하고, 상태 코드를 적절히 반환

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse(
                code = ex.responseCode.code,
                message = ex.getFormattedMessage(),
                data = null
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse(
                code = ex.responseCode.code,
                message = ex.getFormattedMessage(),
                data = null
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse(
                code = ex.responseCode.code,
                message = ex.getFormattedMessage(),
                data = null
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(UnAuthorizedException::class)
    fun handleUnAuthorizedException(ex: UnAuthorizedException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse(
                code = ex.responseCode.code,
                message = ex.getFormattedMessage(),
                data = null
            ),
            HttpStatus.UNAUTHORIZED
        )
    }
}
