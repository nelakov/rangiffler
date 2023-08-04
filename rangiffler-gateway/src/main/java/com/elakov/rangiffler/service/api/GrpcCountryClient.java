package com.elakov.rangiffler.service.api;

import com.elakov.rangiffler.grpc.RangifflerCountryServiceGrpc;
import com.elakov.rangiffler.model.CountryJson;
import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrpcCountryClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountryClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcCountryClient")
    private RangifflerCountryServiceGrpc.RangifflerCountryServiceBlockingStub rangifflerCountryServiceBlockingStub;

    public @Nonnull
    List<CountryJson> getAllCountries() {
        try {
            return rangifflerCountryServiceBlockingStub.getAllCountries(EMPTY).getCountriesList()
                    .stream().map(CountryJson::fromGrpcMessage)
                    .collect(Collectors.toList());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
