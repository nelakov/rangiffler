package com.elakov.rangiffler.model;

import com.elakov.rangiffler.grpc.Country;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CountryJson {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("code")
  private String code;

  @JsonProperty("name")
  private String name;

  public static CountryJson fromGrpcMessage(Country country) {
    return Optional.ofNullable(country.getId())
            .map(id -> CountryJson.builder()
                    .id(UUID.fromString(id))
                    .code(country.getCode())
                    .name(country.getName())
                    .build())
            .orElse(null);
  }

  public Country toGrpcMessage() {
    return Optional.ofNullable(id)
            .map(id -> Country.newBuilder()
                    .setCode(code)
                    .setId(id.toString())
                    .setName(name)
                    .build())
            .orElseGet(() -> Country.newBuilder()
                    .setCode(code)
                    .setName(name)
                    .build());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CountryJson that = (CountryJson) o;
    return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name);
  }
}