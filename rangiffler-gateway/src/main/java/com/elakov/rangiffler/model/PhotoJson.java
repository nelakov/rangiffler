package com.elakov.rangiffler.model;

import com.elakov.grpc.rangiffler.grpc.Photo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public record PhotoJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("country") CountryJson countryJson,
        @JsonProperty("photo") String photo,
        @JsonProperty("description") String description,
        @JsonProperty("username") String username
) {

    public PhotoJson withUsername(String username) {
        return new PhotoJson(id, countryJson, photo, description, username);
    }

    public static PhotoJson fromGrpcMessage(Photo photoGrpc) {
        if (photoGrpc == null) {
            return null;
        }
        return new PhotoJson(
                !photoGrpc.getId().isEmpty() ? UUID.fromString(photoGrpc.getId()) : null,
                CountryJson.fromGrpcMessage(photoGrpc.getCountryCode()),
                photoGrpc.getPhoto(),
                photoGrpc.getDescription(),
                photoGrpc.getUsername()
        );
    }

    public Photo toGrpcMessage() {
        Photo.Builder builder = Photo.newBuilder()
                .setUsername(username)
                .setDescription(description)
                .setPhoto(photo)
                .setCountryCode(countryJson.toGrpcMessage());
        if (id != null) {
            builder.setId(id.toString());
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoJson photoJson = (PhotoJson) o;
        return Objects.equals(id, photoJson.id)
                && Objects.equals(countryJson, photoJson.countryJson)
                && Objects.equals(photo, photoJson.photo)
                && Objects.equals(description, photoJson.description)
                && Objects.equals(username, photoJson.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryJson, photo, description, username);
    }
}
