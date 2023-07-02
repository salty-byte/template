package io.saltybyte.demo.security

import com.auth0.jwt.exceptions.JWTVerificationException
import com.nimbusds.jwt.JWTParser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.text.ParseException
import java.util.*

@Component
class JWTDecoder(private val authHelper: AuthHelper) : JwtDecoder {

    override fun decode(token: String): Jwt {
        return try {
            authHelper.decodeJWT(token) // 冗長
            val parsedJwt = JWTParser.parse(token)
            val headers = parsedJwt.header.toJSONObject()
            val claims = parsedJwt.jwtClaimsSet.claims
            if (claims[JwtClaimNames.IAT] is Date) {
                val iat = claims[JwtClaimNames.IAT] as Date
                claims[JwtClaimNames.IAT] = iat
            }
            if (claims[JwtClaimNames.EXP] is Date) {
                val exp = claims[JwtClaimNames.EXP] as Date
                claims[JwtClaimNames.EXP] = exp
            }
            Jwt.withTokenValue(parsedJwt.parsedString)
                .headers { h -> h.putAll(headers) }
                .claims { c -> c.putAll(claims) }
                .build()
        } catch (e: JWTVerificationException) {
            throw JwtException("Provided token is not valid")
        } catch (e: ParseException) {
            throw JwtException("Provided token is not valid")
        } catch (ex: Exception) {
            throw JwtException("Error during parse token")
        }
    }
}
