package io.saltybyte.demo.security

import io.saltybyte.proto.AuthServiceGrpcKt
import org.lognet.springboot.grpc.security.GrpcSecurity
import org.lognet.springboot.grpc.security.GrpcSecurityConfigurerAdapter
import org.lognet.springboot.grpc.security.jwt.JwtAuthProviderFactory
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder

@Configuration
class GrpcSecurityConfiguration(private val jwtDecoder: JwtDecoder) :
    GrpcSecurityConfigurerAdapter() {

    override fun configure(builder: GrpcSecurity) {
        builder.authorizeRequests()
            .anyServiceExcluding(AuthServiceGrpcKt.serviceDescriptor).authenticated()
            .and()
            .authenticationProvider(JwtAuthProviderFactory.forAuthorities(jwtDecoder))
    }
}
