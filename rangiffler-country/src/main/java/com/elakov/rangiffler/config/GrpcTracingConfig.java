package com.elakov.rangiffler.config;

import com.elakov.grpc.rangiffler.tracing.RequestIdServerInterceptor;
import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;

@Configuration
public class GrpcTracingConfig {

    @Bean
    @GlobalServerInterceptor
    public ServerInterceptor requestIdServerInterceptor() {
        return new RequestIdServerInterceptor();
    }
}
