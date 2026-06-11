package com.elakov.rangiffler.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GrpcExceptionHandlerTest {

    private final GrpcExceptionHandler handler = new GrpcExceptionHandler();

    static Stream<Arguments> codeToHttp() {
        return Stream.of(
                Arguments.of(Status.NOT_FOUND, HttpStatus.NOT_FOUND),
                Arguments.of(Status.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST),
                Arguments.of(Status.FAILED_PRECONDITION, HttpStatus.BAD_REQUEST),
                Arguments.of(Status.OUT_OF_RANGE, HttpStatus.BAD_REQUEST),
                Arguments.of(Status.ALREADY_EXISTS, HttpStatus.CONFLICT),
                Arguments.of(Status.ABORTED, HttpStatus.CONFLICT),
                Arguments.of(Status.UNAUTHENTICATED, HttpStatus.UNAUTHORIZED),
                Arguments.of(Status.PERMISSION_DENIED, HttpStatus.FORBIDDEN),
                Arguments.of(Status.UNIMPLEMENTED, HttpStatus.NOT_IMPLEMENTED),
                Arguments.of(Status.DEADLINE_EXCEEDED, HttpStatus.GATEWAY_TIMEOUT),
                Arguments.of(Status.UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE),
                Arguments.of(Status.INTERNAL, HttpStatus.SERVICE_UNAVAILABLE),
                Arguments.of(Status.UNKNOWN, HttpStatus.SERVICE_UNAVAILABLE)
        );
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("codeToHttp")
    @DisplayName("maps each gRPC status code to the expected HTTP status")
    void mapsStatusToHttp(Status grpcStatus, HttpStatus expectedHttp) {
        StatusRuntimeException ex = grpcStatus.withDescription("boom").asRuntimeException();

        ResponseEntity<Object> response = handler.handleGrpcException(ex);

        assertThat(response.getStatusCode()).isEqualTo(expectedHttp);
    }

    @Test
    @DisplayName("body carries the gRPC code name, description and numeric HTTP status")
    @SuppressWarnings("unchecked")
    void bodyShape() {
        StatusRuntimeException ex = Status.NOT_FOUND.withDescription("no such country").asRuntimeException();

        ResponseEntity<Object> response = handler.handleGrpcException(ex);

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("status", HttpStatus.NOT_FOUND.value());
        assertThat(body).containsEntry("error", "NOT_FOUND");
        assertThat(body).containsEntry("message", "no such country");
        assertThat(body).containsKey("timestamp");
    }
}
