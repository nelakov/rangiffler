package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.CountryByCodeRequest;
import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GrpcCountryClient {
    @GrpcClient("grpcCountryClient")
    private RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub rangifflerCountryServiceBlockingStub;

    public Country getCountryByCode(CountryByCodeRequest countryByCodeRequest) {
        return  rangifflerCountryServiceBlockingStub.getCountryByCode(countryByCodeRequest);
    }
}
