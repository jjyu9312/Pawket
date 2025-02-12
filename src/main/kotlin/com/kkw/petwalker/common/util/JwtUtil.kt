package com.kkw.petwalker.common.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {
    private val secretKey = "your_jwt_secret_key"
    private val algorithm = Algorithm.HMAC256(secretKey)
    private val verifier = JWT.require(algorithm).build()

    fun generateToken(email: String): String {
        return JWT.create()
            .withSubject(email)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000)) // 1일 유효
            .sign(algorithm)
    }

    fun verifyToken(token: String): DecodedJWT {
        return verifier.verify(token)
    }
}