package com.kkw.pawket.common.service

import com.kkw.pawket.user.domain.OAuthProvider
import com.kkw.pawket.user.service.UserService
import com.kkw.pawket.common.exception.IllegalArgumentException
import com.kkw.pawket.common.response.ResponseCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
) : AuthenticationSuccessHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val principal = authentication.principal as DefaultOAuth2User

        // 이메일 정보 추출
        val email = principal.getAttribute<String>("email")
            ?: throw IllegalArgumentException(ResponseCode.EMAIL_NOT_FOUND)

        // 제공자 정보 추출
        val clientRegistration = (authentication.authorities.firstOrNull {
            it is OAuth2AuthenticationToken
        } as? OAuth2AuthenticationToken)?.authorizedClientRegistrationId

        val provider = when(clientRegistration?.lowercase()) {
            "google" -> OAuthProvider.GOOGLE
            "kakao" -> OAuthProvider.KAKAO
            "apple" -> OAuthProvider.APPLE
            else -> throw IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: $clientRegistration")
        }

        // 프로바이더의 사용자 ID 추출
        val providerUserId = when(provider) {
            OAuthProvider.GOOGLE -> principal.getAttribute<String>("sub")
            OAuthProvider.KAKAO -> principal.getAttribute<String>("id")?.toString()
            OAuthProvider.APPLE -> principal.getAttribute<String>("sub")
        } ?: throw IllegalArgumentException("OAuth 제공자 ID를 찾을 수 없습니다")

        // UserService의 메서드를 활용하여 사용자 찾기 또는 생성
        val user = userService.findOrCreateOAuthUser(email, provider, providerUserId)

        // 토큰 생성
        val token = jwtTokenProvider.createToken(user.id, user.email, provider.name)

        logger.info("사용자 로그인 성공: userId=${user.id}, email=${user.email}, provider=$provider")

        // JWT를 클라이언트로 반환
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""
            {
                "accessToken": "$token",
                "userId": "${user.id}",
                "email": "${user.email}",
                "provider": "${provider.name}"
            }
        """.trimIndent())
    }
}