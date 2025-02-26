package com.kkw.petwalker.common.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider {

    // secretKey 및 validityInMilliseconds 값 주입
    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${jwt.validity-in-milliseconds}")
    private var validityInMilliseconds: Long = 0

    // 클래스 초기화 시 secretKey를 Base64 디코딩 후 Key 객체로 변환
    private val key: Key by lazy {
        SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.jcaName)
    }

    /**
     * JWT 토큰 생성
     */
    fun createToken(email: String): String {
        val claims = Jwts.claims().setSubject(email)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)  // Key 객체를 사용
            .compact()
    }

    /**
     * JWT 토큰 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key) // Key 객체를 사용
                .build()
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
        return Jwts.parserBuilder()
            .setSigningKey(key) // Key 객체를 사용
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}
