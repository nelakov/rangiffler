package com.elakov.rangiffler.service;

import com.elakov.grpc.rangiffler.grpc.CountriesResponse;
import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.CountryByCodeRequest;
import com.elakov.rangiffler.data.CountryEntity;
import com.elakov.rangiffler.data.repository.CountryRepository;
import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrpcCountryServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @InjectMocks
    private GrpcCountryService grpcCountryService;

    @Captor
    private ArgumentCaptor<CountriesResponse> countriesCaptor;
    @Captor
    private ArgumentCaptor<Country> countryCaptor;
    @Captor
    private ArgumentCaptor<Throwable> errorCaptor;

    private static CountryEntity country(String code, String name) {
        CountryEntity entity = new CountryEntity();
        entity.setId(UUID.randomUUID());
        entity.setCode(code);
        entity.setName(name);
        return entity;
    }

    @Test
    @DisplayName("getAllCountries streams every country then completes")
    void getAllCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(country("FJ", "Fiji"), country("CA", "Canada")));
        @SuppressWarnings("unchecked")
        StreamObserver<CountriesResponse> observer = org.mockito.Mockito.mock(StreamObserver.class);

        grpcCountryService.getAllCountries(Empty.getDefaultInstance(), observer);

        verify(observer).onNext(countriesCaptor.capture());
        verify(observer).onCompleted();
        verify(observer, never()).onError(org.mockito.ArgumentMatchers.any());
        assertThat(countriesCaptor.getValue().getCountriesList())
                .extracting(Country::getCode)
                .containsExactlyInAnyOrder("FJ", "CA");
    }

    @Test
    @DisplayName("getCountryByCode streams the matching country then completes")
    void getCountryByCodeFound() {
        when(countryRepository.findByCode("FJ")).thenReturn(country("FJ", "Fiji"));
        @SuppressWarnings("unchecked")
        StreamObserver<Country> observer = org.mockito.Mockito.mock(StreamObserver.class);

        grpcCountryService.getCountryByCode(
                CountryByCodeRequest.newBuilder().setCountryCode("FJ").build(), observer);

        verify(observer).onNext(countryCaptor.capture());
        verify(observer).onCompleted();
        assertThat(countryCaptor.getValue().getName()).isEqualTo("Fiji");
        assertThat(countryCaptor.getValue().getCode()).isEqualTo("FJ");
    }

    @Test
    @DisplayName("getCountryByCode errors with NOT_FOUND when the code is unknown")
    void getCountryByCodeMissing() {
        when(countryRepository.findByCode("ZZ")).thenReturn(null);
        @SuppressWarnings("unchecked")
        StreamObserver<Country> observer = org.mockito.Mockito.mock(StreamObserver.class);

        grpcCountryService.getCountryByCode(
                CountryByCodeRequest.newBuilder().setCountryCode("ZZ").build(), observer);

        verify(observer, never()).onNext(org.mockito.ArgumentMatchers.any());
        verify(observer).onError(errorCaptor.capture());
        assertThat(errorCaptor.getValue()).isInstanceOf(StatusRuntimeException.class);
        assertThat(((StatusRuntimeException) errorCaptor.getValue()).getStatus().getCode())
                .isEqualTo(io.grpc.Status.Code.NOT_FOUND);
    }
}
