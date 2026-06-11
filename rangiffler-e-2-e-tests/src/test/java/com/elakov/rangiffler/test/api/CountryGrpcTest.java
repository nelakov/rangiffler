package com.elakov.rangiffler.test.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.rangiffler.data.entity.country.CountryEntity;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.API;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.COUNTRY;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.DB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Owner(ELAKOV)
@Epic("Country service")
@Feature("Countries (gRPC)")
@Tags({@Tag(API), @Tag(COUNTRY), @Tag(DB)})
@DisplayName("[grpc] Country")
class CountryGrpcTest extends BaseGrpcTest {

    @Test
    @DisplayName("getAllCountries returns exactly the countries stored in the DB")
    void getAllCountriesMatchesDatabase() {
        List<Country> fromApi = countryGrpcClient.getAllCountries().getCountriesList();
        List<CountryEntity> fromDb = countryRepository.findAll();

        assertThat(fromApi).isNotEmpty();
        assertThat(fromApi).hasSameSizeAs(fromDb);
        assertThat(fromApi).extracting(Country::getCode)
                .containsExactlyInAnyOrderElementsOf(fromDb.stream().map(CountryEntity::getCode).toList());
    }

    @Test
    @DisplayName("getCountryByCode returns the country matching the DB row (code, name, id)")
    void getCountryByCodeMatchesDatabase() {
        CountryEntity expected = countryRepository.findByCode("FJ");
        assertThat(expected).as("seed data must contain FJ").isNotNull();

        Country actual = countryGrpcClient.getCountryByCode("FJ");

        assertThat(actual.getCode()).isEqualTo(expected.getCode());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(UUID.fromString(actual.getId())).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("getCountryByCode errors with NOT_FOUND for an unknown code")
    void getCountryByCodeUnknownFails() {
        assertThatThrownBy(() -> countryGrpcClient.getCountryByCode("ZZ"))
                .isInstanceOf(StatusRuntimeException.class)
                .extracting(e -> ((StatusRuntimeException) e).getStatus().getCode())
                .isEqualTo(io.grpc.Status.Code.NOT_FOUND);
    }
}
