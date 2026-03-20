package com.elakov.rangiffler.model;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("code") String code,
        @JsonProperty("name") String name
) {

    public static CountryJson fromGrpcMessage(Country country) {
        if (country == null || country.getId().isEmpty()) {
            return null;
        }
        return new CountryJson(
                UUID.fromString(country.getId()),
                country.getCode(),
                country.getName()
        );
    }

    public Country toGrpcMessage() {
        Country.Builder builder = Country.newBuilder()
                .setCode(code)
                .setName(name);
        if (id != null) {
            builder.setId(id.toString());
        }
        return builder.build();
    }
}
