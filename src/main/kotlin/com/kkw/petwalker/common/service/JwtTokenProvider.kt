package com.kkw.petwalker.common.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    secretKey: String,
    private val validityInMilliseconds: Long
) {

    // 역할: JWT 토큰 생성, 유효성 검증, 토큰에서 사용자 정보 추출

    private val secretKey: String = Base64.getEncoder().encodeToString(secretKey.toByteArray())

    fun createToken(email: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(email)
        claims["roles"] = roles

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }
}
