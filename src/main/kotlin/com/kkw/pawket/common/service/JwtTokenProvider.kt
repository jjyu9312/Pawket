package com.kkw.pawket.common.service

import com.kkw.pawket.user.domain.OAuthProvider
import com.kkw.pawket.user.domain.User
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
    fun createToken(id: String, email: String, provider: String): String {
        val claims = Jwts.claims().setSubject(email)
        claims["userId"] = id
        claims["provider"] = provider

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
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
     * JWT 토큰에서 사용자 ID 추출
     */
    fun getUserIdFromToken(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .get("userId", String::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * JWT 토큰에서 OAuth 제공자 추출
     */
    fun getProviderFromToken(token: String): OAuthProvider? {
        return try {
            val providerString = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .get("provider", String::class.java)

            OAuthProvider.valueOf(providerString)
        } catch (e: Exception) {
            null
        }
}
