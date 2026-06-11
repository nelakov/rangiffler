package com.elakov.rangiffler.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps gRPC errors bubbling up from backend services to HTTP responses, so
 * controllers and gRPC clients don't each translate StatusRuntimeException
 * inline (previously only GrpcCountryClient did, inconsistently).
 */
@RestControllerAdvice
public class GrpcExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcExceptionHandler.class);

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Object> handleGrpcException(StatusRuntimeException ex) {
        HttpStatus httpStatus = toHttpStatus(Status.fromThrowable(ex).getCode());
        LOG.error("### gRPC call failed with status {} -> HTTP {}", ex.getStatus().getCode(), httpStatus.value(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", httpStatus.value());
        body.put("error", ex.getStatus().getCode().name());
        body.put("message", ex.getStatus().getDescription());
        return new ResponseEntity<>(body, httpStatus);
    }

    private HttpStatus toHttpStatus(Status.Code code) {
        return switch (code) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT, FAILED_PRECONDITION, OUT_OF_RANGE -> HttpStatus.BAD_REQUEST;
            case ALREADY_EXISTS, ABORTED -> HttpStatus.CONFLICT;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
            case UNIMPLEMENTED -> HttpStatus.NOT_IMPLEMENTED;
            case DEADLINE_EXCEEDED -> HttpStatus.GATEWAY_TIMEOUT;
            default -> HttpStatus.SERVICE_UNAVAILABLE;
        };
    }
}
