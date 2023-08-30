package com.elakov.rangiffler.api.grpc;

import com.elakov.grpc.rangiffler.grpc.CountriesResponse;
import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.CountryByCodeRequest;
import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.rangiffler.config.services.ServicesProperties;
import com.google.protobuf.Empty;

//TODO: Настроить логирование для gRPC
public class CountryGrpcClient extends BaseGrpcClient {

    private RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub countryServiceBlockingStub;
    public CountryGrpcClient() {
        super(ServicesProperties.COUNTRY_GRPC_HOST, ServicesProperties.COUNTRY_GRPC_PORT);
        countryServiceBlockingStub = RangifflerCountryServiceGrpc.newBlockingStub(channel);
    }

    public CountriesResponse getAllCountries() {
        return countryServiceBlockingStub.getAllCountries(Empty.getDefaultInstance());
    }

    public Country getCountryByCode(String countryCode) {
        CountryByCodeRequest req = CountryByCodeRequest.newBuilder()
                .setCountryCode(countryCode)
                .build();
       return countryServiceBlockingStub.getCountryByCode(req);
    }
}
