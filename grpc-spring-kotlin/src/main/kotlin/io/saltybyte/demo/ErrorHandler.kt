package io.saltybyte.demo

import io.grpc.Status
import io.saltybyte.demo.extension.logger
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationServiceException

@GRpcServiceAdvice
internal class ErrorHandler {

    @GRpcExceptionHandler
    fun handle(exc: AuthenticationServiceException, scope: GRpcExceptionScope): Status {
        logger.error("AuthenticationServiceException: ${exc.stackTraceToString()}")
        return Status.fromCode(Status.UNAUTHENTICATED.code).withDescription("許可されていません")
    }

    @GRpcExceptionHandler
    fun handle(exc: AuthenticationCredentialsNotFoundException, scope: GRpcExceptionScope): Status {
        logger.error("AuthenticationCredentialsNotFoundException: ${exc.stackTraceToString()}")
        return Status.fromCode(Status.UNAUTHENTICATED.code).withDescription("許可されていません")
    }

    @GRpcExceptionHandler
    fun handle(exc: Exception, scope: GRpcExceptionScope): Status {
        logger.error("Exception: ${exc.stackTraceToString()}")
        return Status.fromCode(Status.INTERNAL.code).withDescription("システムエラーが発生しました")
    }
}
