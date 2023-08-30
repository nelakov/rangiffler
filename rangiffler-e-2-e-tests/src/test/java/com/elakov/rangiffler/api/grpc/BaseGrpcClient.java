package com.elakov.rangiffler.api.grpc;

import com.elakov.grpc.rangiffler.grpc.Username;
import com.google.protobuf.Empty;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

//TODO: Setting logging для gRPC
public class BaseGrpcClient {

    // By default 4MB
    private final static int MAX_INBOUND_MESSAGE_SIZE = 20_000_000; //20MB
    protected static Channel channel;
    protected static final Empty EMPTY = Empty.getDefaultInstance();

    public BaseGrpcClient(String serviceHost, int servicePort) {
        channel = ManagedChannelBuilder.forAddress(serviceHost, servicePort)
                .maxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE)
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    protected Username getUsername(String username) {
        return Username.newBuilder()
                .setUsername(username)
                .build();
    }

}
