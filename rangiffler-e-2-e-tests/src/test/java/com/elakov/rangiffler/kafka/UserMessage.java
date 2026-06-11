package com.elakov.rangiffler.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payload of the `users` topic — mirrors rangiffler-auth's UserJson.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserMessage(
        @JsonProperty("username") String username
) {
}
