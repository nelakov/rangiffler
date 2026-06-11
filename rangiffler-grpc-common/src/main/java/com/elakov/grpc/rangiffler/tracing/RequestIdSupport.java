package com.elakov.grpc.rangiffler.tracing;

import io.grpc.Metadata;

/**
 * Shared contract for request-id propagation across the gRPC mesh:
 * the MDC key the services log under and the gRPC metadata header the
 * id travels in. Lives in grpc-common so all participants agree on the wire.
 */
public final class RequestIdSupport {

    public static final String MDC_KEY = "requestId";

    public static final Metadata.Key<String> REQUEST_ID_METADATA =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    private RequestIdSupport() {
    }
}
