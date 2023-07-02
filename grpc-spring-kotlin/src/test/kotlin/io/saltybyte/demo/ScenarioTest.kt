package io.saltybyte.demo;

import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.saltybyte.proto.Auth
import io.saltybyte.proto.AuthServiceGrpcKt
import io.saltybyte.proto.HelloServiceGrpcKt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ScenarioTest {

    @LocalRunningGrpcPort
    private val runningPort = 6565

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun hello() = runTest {
        val channel = ManagedChannelBuilder.forAddress("localhost", runningPort)
            .usePlaintext()
            .build()
        val authStub = AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
        val name = "Test"
        val generateTokenRequest = Auth.GenerateTokenRequest
            .newBuilder()
            .setName(name)
            .build()

        val generateTokenResponse: Auth.GenerateTokenResponse =
            authStub.generateToken(generateTokenRequest)
        val token = generateTokenResponse.token

        val helloStub = HelloServiceGrpcKt.HelloServiceCoroutineStub(channel)
        val key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
        val helloResponse = helloStub.hello(
            Empty.getDefaultInstance(),
            Metadata().apply { put(key, "Bearer $token") }
        )
        assertThat(helloResponse.message).isEqualTo("Hello, Test!")
    }
}
