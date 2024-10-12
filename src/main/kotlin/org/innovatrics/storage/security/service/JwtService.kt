package org.innovatrics.storage.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService {


    @Value("\${jwt.secret}")
    private lateinit var SECRET_KEY: String

    @Value("\${jwt.expiration}")
    private var EXPIRATION: Long = 1000 * 60 * 60

    fun createJwtToken(userId: String): String {
        return Jwts.builder()
            .subject(userId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(getSignInKey())
            .compact()
    }

    fun parseJwtToken(token: String): Claims? {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload

    }

    private fun getSignInKey(): SecretKey {
        val bytes: ByteArray = Base64.getDecoder()
            .decode(SECRET_KEY)
        return SecretKeySpec(bytes, "HmacSHA256")
    }
}
