package com.kkw.petwalker.user.controller

import com.kkw.petwalker.common.response.ApiResponse
import com.kkw.petwalker.common.response.ApiResponseFactory
import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.user.dto.CreateUserDto
import com.kkw.petwalker.user.dto.LoginUserDto
import com.kkw.petwalker.user.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userService: UserService,
) {

    @GetMapping("/login")
    fun login(@RequestParam provider: String, response: HttpServletResponse) {
        val redirectUrl = userService.getOAuthRedirectUrl(provider)
        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/oauth/callback/{provider}")
    fun oauthCallback(
        @PathVariable provider: String,
        @RequestParam code: String
    ): ResponseEntity<LoginUserDto> {
        val loginUser = userService.handleOAuthCallback(provider, code)
        return ResponseEntity.ok(loginUser)
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse<String>> {
        return try {
            val success = userService.logout()
            ApiResponseFactory.success(success)
        } catch (e: Exception) {
            ApiResponseFactory.error(ResponseCode.INTERNAL_SERVER_ERROR, customMessage = e.message)
        }
    }

    @PostMapping("")
    fun createOwner(req: CreateUserDto.Req): ResponseEntity<ApiResponse<String>> {
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