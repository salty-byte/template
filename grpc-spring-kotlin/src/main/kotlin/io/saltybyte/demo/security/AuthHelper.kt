package io.saltybyte.demo.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.grpc.Context
import org.lognet.springboot.grpc.security.GrpcSecurity
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class AuthHelper {

    @Value("\${app.auth.secret}")
    private val SECRET = ""

    @Value("\${app.auth.issuer}")
    private val ISSUER = ""

    private val CLAIM_CLIENT_NAME = "name"

    fun currentClientName(): String {
        return getToken().getClaimAsString(CLAIM_CLIENT_NAME)
    }

    private fun getToken(): Jwt {
        val authentication = getAuthentication()
        return authentication.token
    }

    private fun getAuthentication(): JwtAuthenticationToken {
        return GrpcSecurity.AUTHENTICATION_CONTEXT_KEY[Context.current()] as JwtAuthenticationToken
    }

    fun generateJWT(name: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withClaim(CLAIM_CLIENT_NAME, name)
            .sign(Algorithm.HMAC256(SECRET))
    }

    fun decodeJWT(token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token)
    }
}
