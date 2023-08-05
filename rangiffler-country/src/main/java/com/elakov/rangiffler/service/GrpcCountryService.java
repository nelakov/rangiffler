package com.elakov.rangiffler.service;

import com.elakov.grpc.rangiffler.grpc.CountriesResponse;
import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.CountryByCodeRequest;
import com.elakov.grpc.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.rangiffler.data.CountryEntity;
import com.elakov.rangiffler.data.repository.CountryRepository;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class GrpcCountryService extends RangifflerCountryServiceGrpc.RangifflerCountryServiceImplBase {

    private CountryRepository countryRepository;

    @Autowired
    public GrpcCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllCountries(Empty request, StreamObserver<CountriesResponse> responseObserver) {
        List<CountryEntity> allCountries = countryRepository.findAll();

        CountriesResponse countriesResponse = CountriesResponse.newBuilder()
                .addAllCountries(allCountries.stream().map(ce -> Country.newBuilder()
                        .setId(ce.getId().toString())
                        .setCode(ce.getCode())
                        .setName(ce.getName())
                        .build())
                        .toList())
                .build();
        responseObserver.onNext(countriesResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getCountryByCode(CountryByCodeRequest countryByCodeRequest, StreamObserver<Country> responseObserver) {
        Optional<CountryEntity> countryEntityOptional =
                Optional.ofNullable(countryRepository.findByCode(countryByCodeRequest.getCountryCode()));

        countryEntityOptional.ifPresent(ce -> {
            Country response = Country.newBuilder()
                    .setId(ce.getId().toString())
                    .setName(ce.getName())
                    .setCode(ce.getCode())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        });

        if (countryEntityOptional.isEmpty()) {
            String errorMessage = "Country with code " + countryByCodeRequest.getCountryCode() + " not found.";
            Status status = Status.NOT_FOUND.withDescription(errorMessage);
            responseObserver.onError(status.asRuntimeException());
        }
    }

}
