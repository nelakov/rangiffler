package com.elakov.rangiffler.config;

import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.elakov.grpc.rangiffler.tracing.RequestIdClientInterceptor;
import io.grpc.ClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientsConfig {

    @Bean
    public RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub countryBlockingStub(GrpcChannelFactory channels) {
        return RangifflerCountryServiceGrpc.newBlockingStub(channels.createChannel("grpcCountryClient"));
    }

    @Bean
    public RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoBlockingStub(GrpcChannelFactory channels) {
        return RangifflerPhotoServiceGrpc.newBlockingStub(channels.createChannel("grpcPhotoClient"));
    }

    @Bean
    @GlobalClientInterceptor
    public ClientInterceptor requestIdClientInterceptor() {
        return new RequestIdClientInterceptor();
    }
}
