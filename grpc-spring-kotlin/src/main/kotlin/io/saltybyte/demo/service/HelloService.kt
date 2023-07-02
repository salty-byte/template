package io.saltybyte.demo.service

import com.google.protobuf.Empty
import io.saltybyte.demo.security.AuthHelper
import io.saltybyte.proto.Hello
import io.saltybyte.proto.HelloServiceGrpcKt
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class HelloService(private var helper: AuthHelper) :
    HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {

    override suspend fun hello(request: Empty): Hello.HelloResponse {
        val name = helper.currentClientName()
        val message = "Hello, $name!"
        return Hello.HelloResponse
            .newBuilder()
            .setMessage(message)
            .build()
    }
}
