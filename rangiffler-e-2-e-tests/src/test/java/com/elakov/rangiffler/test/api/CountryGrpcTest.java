package com.elakov.rangiffler.test.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.helper.AllureSoftSteps;
import com.elakov.rangiffler.helper.comparator.JsonComparator;
import com.elakov.rangiffler.model.CountryJson;
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

        new AllureSoftSteps()
                .add("countries are returned", () -> assertThat(fromApi).isNotEmpty())
                .add("count matches the DB", () -> assertThat(fromApi).hasSameSizeAs(fromDb))
                .add("codes match the DB", () -> assertThat(fromApi).extracting(Country::getCode)
                        .containsExactlyInAnyOrderElementsOf(fromDb.stream().map(CountryEntity::getCode).toList()))
                .execute();
    }

    @Test
    @DisplayName("getCountryByCode returns the country matching the DB row (code, name, id)")
    void getCountryByCodeMatchesDatabase() {
        CountryEntity expected = countryRepository.findByCode("FJ");
        assertThat(expected).as("seed data must contain FJ").isNotNull();

        Country actual = countryGrpcClient.getCountryByCode("FJ");

        new AllureSoftSteps()
                .add("code matches the DB", () -> assertThat(actual.getCode()).isEqualTo(expected.getCode()))
                .add("name matches the DB", () -> assertThat(actual.getName()).isEqualTo(expected.getName()))
                .add("id matches the DB", () -> assertThat(UUID.fromString(actual.getId())).isEqualTo(expected.getId()))
                .execute();
    }

    @Test
    @DisplayName("getCountryByCode errors with NOT_FOUND for an unknown code")
    void getCountryByCodeUnknownFails() {
        assertThatThrownBy(() -> countryGrpcClient.getCountryByCode("ZZ"))
                .isInstanceOf(StatusRuntimeException.class)
                .extracting(e -> ((StatusRuntimeException) e).getStatus().getCode())
                .isEqualTo(io.grpc.Status.Code.NOT_FOUND);
    }

    @Test
    @DisplayName("getCountryByCode body matches the expected JSON (structural diff in Allure)")
    void getCountryByCodeBodyMatchesExpectedJson() {
        CountryJson country = CountryJson.fromGrpcMessage(countryGrpcClient.getCountryByCode("FJ"));

        // id is server-generated → ignored; code and name compared structurally.
        String expected = """
                {
                  "code": "FJ",
                  "name": "Fiji"
                }""";

        new JsonComparator()
                .assertThatObject(country)
                .ignorePaths("id")
                .equalsToJson(expected);
    }
}
