package com.elakov.rangiffler.controller;

import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.service.api.GrpcCountryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {
  private final GrpcCountryClient grpcCountryClient;

  @Autowired
  public CountryController(GrpcCountryClient grpcCountryClient) {
    this.grpcCountryClient = grpcCountryClient;
  }

  @GetMapping("/countries")
  public List<CountryJson> getAllCountries() {
    return grpcCountryClient.getAllCountries();
  }

}