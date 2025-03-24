package com.kkw.petwalker.common.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler { // 역할: OAuth2 로그인 성공 시 JWT 토큰을 생성하여 클라이언트로 반환하는 역할
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val principal = authentication.principal
                as org.springframework.security.oauth2.core.user.DefaultOAuth2User
        val email = principal.getAttribute<String>("email")

        val token = jwtTokenProvider.createToken(email!!)

        // JWT를 클라이언트로 반환 (예: JSON 응답)
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{"token": "$token"}""")
    }
}
