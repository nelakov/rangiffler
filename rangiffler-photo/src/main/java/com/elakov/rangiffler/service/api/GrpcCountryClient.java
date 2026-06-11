package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.CountryByCodeRequest;
import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import org.springframework.stereotype.Component;

@Component
public class GrpcCountryClient {

    private final RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub rangifflerCountryServiceBlockingStub;

    public GrpcCountryClient(RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub countryBlockingStub) {
        this.rangifflerCountryServiceBlockingStub = countryBlockingStub;
    }

    public Country getCountryByCode(CountryByCodeRequest countryByCodeRequest) {
        return rangifflerCountryServiceBlockingStub.getCountryByCode(countryByCodeRequest);
    }
}
