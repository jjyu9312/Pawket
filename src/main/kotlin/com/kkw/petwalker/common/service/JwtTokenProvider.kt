package com.kkw.petwalker.common.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

class JwtTokenProvider(
    secretKey: String,
    private val validityInMilliseconds: Long
) {
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
