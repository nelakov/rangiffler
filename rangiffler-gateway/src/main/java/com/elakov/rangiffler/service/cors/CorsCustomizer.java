package com.elakov.rangiffler.service.cors;

import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Component
public class CorsCustomizer {

  private final String rangifflerClientUri;

  @Autowired
  public CorsCustomizer(@Value("${rangiffler-client.base-uri}") String rangifflerClientUri) {
    this.rangifflerClientUri = rangifflerClientUri;
  }

  public void corsCustomizer(@Nonnull HttpSecurity http) throws Exception {
    http.cors(c -> {
      CorsConfigurationSource source = s -> {
        CorsConfiguration cc = new CorsConfiguration();
        cc.setAllowCredentials(true);
        cc.setAllowedOrigins(List.of(rangifflerClientUri));
        cc.setAllowedHeaders(List.of("*"));
        cc.setAllowedMethods(List.of("*"));
        return cc;
      };

      c.configurationSource(source);
    });
  }
}
