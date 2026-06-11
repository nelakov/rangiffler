package com.elakov.rangiffler.model;

import com.elakov.grpc.rangiffler.grpc.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CountryJsonTest {

    @Test
    @DisplayName("fromGrpcMessage maps id, code and name")
    void fromGrpcMessage() {
        UUID id = UUID.randomUUID();
        Country grpc = Country.newBuilder().setId(id.toString()).setCode("FJ").setName("Fiji").build();

        CountryJson json = CountryJson.fromGrpcMessage(grpc);

        assertThat(json.id()).isEqualTo(id);
        assertThat(json.code()).isEqualTo("FJ");
        assertThat(json.name()).isEqualTo("Fiji");
    }

    @Test
    @DisplayName("fromGrpcMessage returns null for a null message or empty id")
    void fromGrpcMessageNullCases() {
        assertThat(CountryJson.fromGrpcMessage(null)).isNull();
        assertThat(CountryJson.fromGrpcMessage(Country.newBuilder().setCode("FJ").build())).isNull();
    }

    @Test
    @DisplayName("toGrpcMessage round-trips id, code and name")
    void toGrpcMessageWithId() {
        UUID id = UUID.randomUUID();
        Country grpc = new CountryJson(id, "FJ", "Fiji").toGrpcMessage();

        assertThat(grpc.getId()).isEqualTo(id.toString());
        assertThat(grpc.getCode()).isEqualTo("FJ");
        assertThat(grpc.getName()).isEqualTo("Fiji");
    }

    @Test
    @DisplayName("toGrpcMessage leaves id blank when absent")
    void toGrpcMessageWithoutId() {
        Country grpc = new CountryJson(null, "FJ", "Fiji").toGrpcMessage();

        assertThat(grpc.getId()).isEmpty();
        assertThat(grpc.getCode()).isEqualTo("FJ");
    }
}
