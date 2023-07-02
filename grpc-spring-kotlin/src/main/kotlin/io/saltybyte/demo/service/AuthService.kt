package io.saltybyte.demo.service

import io.saltybyte.demo.security.AuthHelper
import io.saltybyte.proto.Auth
import io.saltybyte.proto.AuthServiceGrpcKt
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class AuthService(private var helper: AuthHelper) :
    AuthServiceGrpcKt.AuthServiceCoroutineImplBase() {

    override suspend fun generateToken(request: Auth.GenerateTokenRequest): Auth.GenerateTokenResponse {
        val name = request.name
        if (name.isNullOrEmpty()) {
            return Auth.GenerateTokenResponse.newBuilder().build()
        }

        val token = helper.generateJWT(name)
        return Auth.GenerateTokenResponse
            .newBuilder()
            .setToken(token)
            .build()
    }
}
