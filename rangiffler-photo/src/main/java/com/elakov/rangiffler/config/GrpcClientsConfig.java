package com.elakov.rangiffler.config;

import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.elakov.grpc.rangiffler.tracing.RequestIdClientInterceptor;
import com.elakov.grpc.rangiffler.tracing.RequestIdServerInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.grpc.server.GlobalServerInterceptor;

@Configuration
public class GrpcClientsConfig {

    @Bean
    public RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub countryBlockingStub(GrpcChannelFactory channels) {
        return RangifflerCountryServiceGrpc.newBlockingStub(channels.createChannel("grpcCountryClient"));
    }

    @Bean
    public RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub userdataBlockingStub(GrpcChannelFactory channels) {
        return RangifflerUserdataServiceGrpc.newBlockingStub(channels.createChannel("grpcUserdataClient"));
    }

    // Incoming requests from the gateway carry the request-id; bind it to MDC...
    @Bean
    @GlobalServerInterceptor
    public ServerInterceptor requestIdServerInterceptor() {
        return new RequestIdServerInterceptor();
    }

    // ...and forward it on the outgoing calls photo makes to country/userdata.
    @Bean
    @GlobalClientInterceptor
    public ClientInterceptor requestIdClientInterceptor() {
        return new RequestIdClientInterceptor();
    }
}
