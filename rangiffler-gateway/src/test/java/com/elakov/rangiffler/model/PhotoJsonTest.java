package com.elakov.rangiffler.model;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.Photo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoJsonTest {

    @Test
    @DisplayName("fromGrpcMessage maps fields and nested country")
    void fromGrpcMessage() {
        UUID id = UUID.randomUUID();
        UUID countryId = UUID.randomUUID();
        Photo grpc = Photo.newBuilder()
                .setId(id.toString())
                .setUsername("bob")
                .setDescription("trip")
                .setPhoto("img")
                .setCountryCode(Country.newBuilder().setId(countryId.toString()).setCode("FJ").setName("Fiji").build())
                .build();

        PhotoJson json = PhotoJson.fromGrpcMessage(grpc);

        assertThat(json.id()).isEqualTo(id);
        assertThat(json.username()).isEqualTo("bob");
        assertThat(json.description()).isEqualTo("trip");
        assertThat(json.photo()).isEqualTo("img");
        assertThat(json.countryJson().code()).isEqualTo("FJ");
    }

    @Test
    @DisplayName("fromGrpcMessage returns null for a null message")
    void fromGrpcMessageNull() {
        assertThat(PhotoJson.fromGrpcMessage(null)).isNull();
    }

    @Test
    @DisplayName("fromGrpcMessage leaves id null when the grpc id is empty")
    void fromGrpcMessageNoId() {
        Photo grpc = Photo.newBuilder()
                .setUsername("bob")
                .setCountryCode(Country.newBuilder().setId(UUID.randomUUID().toString()).setCode("FJ").build())
                .build();

        assertThat(PhotoJson.fromGrpcMessage(grpc).id()).isNull();
    }

    @Test
    @DisplayName("toGrpcMessage round-trips the photo with its nested country")
    void toGrpcMessage() {
        UUID id = UUID.randomUUID();
        PhotoJson json = new PhotoJson(id, new CountryJson(null, "FJ", "Fiji"), "img", "trip", "bob");

        Photo grpc = json.toGrpcMessage();

        assertThat(grpc.getId()).isEqualTo(id.toString());
        assertThat(grpc.getUsername()).isEqualTo("bob");
        assertThat(grpc.getCountryCode().getCode()).isEqualTo("FJ");
    }

    @Test
    @DisplayName("withUsername replaces only the username")
    void withUsername() {
        PhotoJson original = new PhotoJson(null, new CountryJson(null, "FJ", "Fiji"), "img", "trip", "bob");

        PhotoJson renamed = original.withUsername("alice");

        assertThat(renamed.username()).isEqualTo("alice");
        assertThat(renamed.description()).isEqualTo("trip");
        assertThat(renamed.countryJson()).isEqualTo(original.countryJson());
    }
}
