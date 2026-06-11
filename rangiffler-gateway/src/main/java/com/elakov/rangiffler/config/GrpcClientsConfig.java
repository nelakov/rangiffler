package com.elakov.rangiffler.config;

import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
