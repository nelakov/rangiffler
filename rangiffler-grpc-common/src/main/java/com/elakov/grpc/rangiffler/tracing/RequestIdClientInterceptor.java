package com.elakov.grpc.rangiffler.tracing;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import org.slf4j.MDC;

/**
 * Copies the current MDC request-id onto outgoing gRPC metadata, so the
 * callee can continue the same trace. start() runs on the caller thread
 * (blocking stub), where the id was placed by the gateway's servlet filter
 * or by {@link RequestIdServerInterceptor} on an upstream hop.
 *
 * Limitation: this relies on the request-id being in MDC on the thread that
 * calls start(). That holds for blocking stubs (the only kind used here). With
 * async/future stubs start() may run on a different thread and the id would be
 * lost — pass it explicitly via CallOptions/Context in that case.
 */
public class RequestIdClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String requestId = MDC.get(RequestIdSupport.MDC_KEY);
                if (requestId != null && !requestId.isBlank()) {
                    headers.put(RequestIdSupport.REQUEST_ID_METADATA, requestId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
