package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.rangiffler.model.CountryJson;
import com.google.protobuf.Empty;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrpcCountryClient {

    private static final Empty EMPTY = Empty.getDefaultInstance();

    private final RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub rangifflerCountryServiceBlockingStub;

    public GrpcCountryClient(RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub countryBlockingStub) {
        this.rangifflerCountryServiceBlockingStub = countryBlockingStub;
    }

    public @Nonnull
    List<CountryJson> getAllCountries() {
        return rangifflerCountryServiceBlockingStub.getAllCountries(EMPTY).getCountriesList()
                .stream().map(CountryJson::fromGrpcMessage)
                .collect(Collectors.toList());
    }
}
