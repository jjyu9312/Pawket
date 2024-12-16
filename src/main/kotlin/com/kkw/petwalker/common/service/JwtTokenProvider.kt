package com.kkw.petwalker.common.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    // secretKey 및 validityInMilliseconds 값 주입
    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${jwt.validity-in-milliseconds}")
    private var validityInMilliseconds: Long = 0

    // 클래스 초기화 시 secretKey를 Base64 인코딩
    init {
        // secretKey를 Base64로 인코딩하여 재설정
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    /**
     * JWT 토큰 생성
     */
    fun createToken(email: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(email)
        claims["roles"] = roles // 사용자 역할 설정

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    /**
     * JWT 토큰 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)

            !claims.body.expiration.before(Date()) // 만료 여부 확인
        } catch (e: Exception) {
            false
        }
    }

    /**
     * JWT 토큰에서 이메일(사용자 정보) 추출
     */
    fun getEmailFromToken(token: String): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject
    }
}
