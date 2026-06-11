package com.elakov.grpc.rangiffler.tracing;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Reads the request-id from incoming gRPC metadata (or mints one if absent)
 * and binds it to MDC for the duration of each listener callback — so the
 * service's logs carry it, and a downstream gRPC call made on the same thread
 * (blocking stub) can read it back via {@link RequestIdClientInterceptor}.
 *
 * MDC is set/cleared around every callback because the listener may run on a
 * gRPC executor thread distinct from interceptCall's thread.
 */
public class RequestIdServerInterceptor implements ServerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestIdServerInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String incoming = headers.get(RequestIdSupport.REQUEST_ID_METADATA);
        String requestId = (incoming == null || incoming.isBlank())
                ? UUID.randomUUID().toString()
                : incoming;
        String method = call.getMethodDescriptor().getFullMethodName();

        // Access log: one line per call carrying the request-id (MDC -> ECS field).
        // Skip gRPC infra plumbing (health probes, reflection) — those are polled
        // continuously and would flood the logs without business value.
        if (!isInfrastructureCall(method)) {
            runWithRequestId(requestId, () -> LOG.info("gRPC call: {}", method));
        }

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                runWithRequestId(requestId, () -> super.onMessage(message));
            }

            @Override
            public void onHalfClose() {
                runWithRequestId(requestId, super::onHalfClose);
            }

            @Override
            public void onCancel() {
                runWithRequestId(requestId, super::onCancel);
            }

            @Override
            public void onComplete() {
                runWithRequestId(requestId, super::onComplete);
            }

            @Override
            public void onReady() {
                runWithRequestId(requestId, super::onReady);
            }
        };
    }

    private static boolean isInfrastructureCall(String fullMethodName) {
        return fullMethodName.startsWith("grpc.health.")
                || fullMethodName.startsWith("grpc.reflection.");
    }

    /** Binds the request-id to MDC for the action, then always clears it — the
     * listener may run on a pooled gRPC executor thread, so leaving it set would
     * leak the id into the next call on that thread. */
    private static void runWithRequestId(String requestId, Runnable action) {
        MDC.put(RequestIdSupport.MDC_KEY, requestId);
        try {
            action.run();
        } finally {
            MDC.remove(RequestIdSupport.MDC_KEY);
        }
    }
}
