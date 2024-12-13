package com.kkw.petwalker.common.service

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as org.springframework.security.oauth2.core.user.DefaultOAuth2User
        val email = principal.getAttribute<String>("email")
        val roles = listOf("ROLE_USER") // 기본 롤 추가

        val token = jwtTokenProvider.createToken(email, roles)

        // JWT를 클라이언트로 반환 (예: JSON 응답)
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{"token": "$token"}""")
    }
}
