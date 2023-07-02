package io.saltybyte.demo.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.StatusException
import io.saltybyte.demo.security.AuthHelper
import io.saltybyte.proto.HelloServiceGrpcKt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class HelloServiceTest() {

    @LocalRunningGrpcPort
    private val runningPort = 6565

    @Autowired
    private lateinit var helper: AuthHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun hello() {
        val channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
            .usePlaintext()
            .build()

        val token = helper.generateJWT("Test")
        val stub = HelloServiceGrpcKt.HelloServiceCoroutineStub(channel)
        val key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)

        runTest {
            val response = stub.hello(
                Empty.getDefaultInstance(),
                Metadata().apply { put(key, "Bearer $token") }
            )
            assertThat(response.message).isEqualTo("Hello, Test!")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun unauthorized() {
        val channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
            .usePlaintext()
            .build()

        val token = JWT.create()
            .withIssuer("io.saltybyte")
            .withClaim("name", "Test")
            .sign(Algorithm.HMAC256("FAKE_SECRET"))
        val stub = HelloServiceGrpcKt.HelloServiceCoroutineStub(channel)
        val key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)

        val error = assertThrows(StatusException::class.java) {
            runTest {
                stub.hello(
                    Empty.getDefaultInstance(),
                    Metadata().apply { put(key, "Bearer $token") }
                )
            }
        }
        assertThat(error.message).isEqualTo("UNAUTHENTICATED: 許可されていません")
    }
}