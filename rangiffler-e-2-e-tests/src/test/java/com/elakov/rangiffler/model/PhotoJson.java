package com.elakov.rangiffler.model;

import com.elakov.grpc.rangiffler.grpc.Photo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
public class PhotoJson {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("country")
  private CountryJson countryJson;

  @JsonProperty("photo")
  private String photo;

  @JsonProperty("description")
  private String description;

  @JsonProperty("username")
  private String username;

  public static PhotoJson fromGrpcMessage(Photo photoGrpc) {
    return Optional.ofNullable(photoGrpc)
            .map(photo -> PhotoJson.builder()
                    .id(!photo.getId().isEmpty() ? UUID.fromString(photo.getId()) : null)
                    .photo(photo.getPhoto())
                    .countryJson(CountryJson.fromGrpcMessage(photo.getCountryCode()))
                    .description(photo.getDescription())
                    .username(photo.getUsername())
                    .build())
            .orElse(null);
  }

  public Photo toGrpcMessage() {
    return Optional.ofNullable(id)
            .map(id -> Photo.newBuilder()
                    .setId(id.toString())
                    .setUsername(username)
                    .setDescription(description)
                    .setPhoto(photo)
                    .setCountryCode(countryJson.toGrpcMessage())
                    .build())
            .orElseGet(() -> Photo.newBuilder()
                    .setUsername(username)
                    .setDescription(description)
                    .setPhoto(photo)
                    .setCountryCode(countryJson.toGrpcMessage())
                    .build());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PhotoJson photoJson = (PhotoJson) o;
    return Objects.equals(id, photoJson.id) && Objects.equals(countryJson, photoJson.countryJson) && Objects.equals(photo, photoJson.photo) && Objects.equals(description, photoJson.description) && Objects.equals(username, photoJson.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, countryJson, photo, description, username);
  }
}