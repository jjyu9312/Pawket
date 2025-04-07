package com.kkw.pawket.common.interceptor

import com.kkw.pawket.common.service.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthorizationInterceptor(
    private val jwtTokenProvider: JwtTokenProvider
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.replace("Bearer ", "")

            // 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰에서 사용자 ID 추출
                val userId = jwtTokenProvider.getUserIdFromToken(token)
                val email = jwtTokenProvider.getUserEmailFromToken(token)

                if (userId != null && email != null) {
                    // 요청 속성에 사용자 정보 저장
                    request.setAttribute("userId", userId)
                    request.setAttribute("email", email)
                    return true
                }
            }

            // 토큰이 유효하지 않거나 필수 정보가 없는 경우
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("{\"message\":\"유효하지 않은 인증 정보입니다.\"}")
            return false
        }

        // 인증이 필요한 경로인 경우 요청 거부
        if (isAuthRequiredPath(request.requestURI)) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("{\"message\":\"인증이 필요합니다.\"}")
            return false
        }

        return true
    }

    private fun isAuthRequiredPath(path: String): Boolean {
        // 인증이 필요한 경로 확인 로직
        val publicPaths = listOf("/public/", "/auth/")
        return publicPaths.none { path.startsWith(it) }
    }
}