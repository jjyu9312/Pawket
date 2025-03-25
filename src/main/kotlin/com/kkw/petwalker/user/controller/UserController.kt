package com.kkw.pawket.user.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.user.model.req.CreateUserReq
import com.kkw.petwalker.user.model.res.CreateUserRes
import com.kkw.petwalker.user.model.res.LoginUserRes
import com.kkw.petwalker.user.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userService: UserService,
) {

    // 로그인 요청을 처리하는 API
    @GetMapping("/login")
    fun login(@RequestParam provider: String, response: HttpServletResponse) {
        val redirectUrl = userService.getOAuthRedirectUrl(provider)
        response.sendRedirect(redirectUrl)
    }

    // OAuth 콜백 요청을 처리하는 API
    @GetMapping("/oauth/callback/{provider}")
    fun oauthCallback(
        @PathVariable provider: String,
        @RequestParam code: String
    ): ResponseEntity<ApiResponse<LoginUserRes>> {
        return try {
            val loginUser = userService.handleOAuthCallback(provider, code)
            ApiResponseFactory.success(loginUser)
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse<String>> {
        return try {
            val success = userService.logout()
            ApiResponseFactory.success(success)
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

    @PostMapping("")
    fun createUser(req: CreateUserReq): ResponseEntity<ApiResponse<CreateUserRes>> {
        return try {
            val userId = userService.createUser(req)
            ApiResponseFactory.success(userId)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                customMessage = e.message
            )
        }
        catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }
}