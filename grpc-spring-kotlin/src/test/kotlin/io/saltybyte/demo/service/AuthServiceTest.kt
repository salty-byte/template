package io.saltybyte.demo.service

import io.grpc.ManagedChannelBuilder
import io.saltybyte.demo.security.AuthHelper
import io.saltybyte.proto.Auth
import io.saltybyte.proto.AuthServiceGrpcKt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class AuthServiceTest {

    @LocalRunningGrpcPort
    private val runningPort = 6565

    @Autowired
    private lateinit var helper: AuthHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun generateToken() {
        val channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
            .usePlaintext()
            .build()
        val stub = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
        val name = "Test"
        val request = Auth.GenerateTokenRequest
            .newBuilder()
            .setName(name)
            .build()

        runTest {
            val response: Auth.GenerateTokenResponse = stub.generateToken(request)
            val receivedName = helper.decodeJWT(response.token).getClaim("name").asString()
            assertThat(receivedName).isEqualTo("Test")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emptyTokenWithoutName() {
        val channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
            .usePlaintext()
            .build()
        val stub = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
        val request = Auth.GenerateTokenRequest
            .newBuilder()
            .build()

        runTest {
            val response: Auth.GenerateTokenResponse = stub.generateToken(request)
            assertThat(response.token).isEmpty()
        }
    }
}
