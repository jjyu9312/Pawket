package com.kkw.pawket.user.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.user.model.req.CreateUserReq
import com.kkw.pawket.user.model.res.CreateUserRes
import com.kkw.pawket.user.model.res.LoginUserRes
import com.kkw.pawket.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "사용자 관련 API")
class UserController (
    private val userService: UserService,
) {

    @Operation(
        summary = "OAuth 로그인",
        description = "선택한 OAuth 제공자의 로그인 페이지로 리다이렉트합니다."
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "302",
                description = "OAuth 제공자 로그인 페이지로 리다이렉트"
            )
        ]
    )
    @GetMapping("/login")
    fun login(
        @Parameter(description = "OAuth 제공자 (예: google, kakao, naver)", required = true)
        @RequestParam provider: String,
        response: HttpServletResponse
    ) {
        val redirectUrl = userService.getOAuthRedirectUrl(provider)
        response.sendRedirect(redirectUrl)
    }

    @Operation(
        summary = "OAuth 콜백 처리",
        description = "OAuth 제공자로부터 리다이렉트된 요청을 처리하고 사용자 정보를 반환합니다."
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [Content(schema = Schema(implementation = LoginUserRes::class))]
            ),
            SwaggerApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            )
        ]
    )
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

    @Operation(
        summary = "로그아웃",
        description = "현재 로그인된 사용자를 로그아웃 처리합니다."
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "로그아웃 성공",
                content = [Content(schema = Schema(implementation = String::class))]
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            ),
            SwaggerApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            )
        ]
    )
    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse<String>> {
        return try {
            val success = userService.logout()
            ApiResponseFactory.success(success)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    @Operation(
        summary = "추가 회원가입",
        description = "OAuth 로그인 후 필요한 추가 정보를 등록합니다."
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "사용자 정보 등록 성공",
                content = [Content(schema = Schema(implementation = CreateUserRes::class))]
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            ),
            SwaggerApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [Content(schema = Schema(implementation = ApiResponse::class))]
            )
        ]
    )
    @PostMapping("/{userId}")
    fun createUser(
        @Parameter(description = "사용자 ID", required = true)
        @PathVariable userId: String,
        @Parameter(description = "사용자 추가 정보", required = true)
        @RequestBody req: CreateUserReq
    ): ResponseEntity<ApiResponse<CreateUserRes>> {
        return try {
            val user = userService.createUser(userId, req)
            ApiResponseFactory.success(user)
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