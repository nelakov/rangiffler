package com.elakov.rangiffler.controller;

import com.elakov.rangiffler.service.api.GrpcGeoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

  private final GrpcGeoClient grpcGeoClient;

  @Autowired
  public CountryController(GrpcGeoClient grpcGeoClient) {
    this.grpcGeoClient = grpcGeoClient;
  }

// TODO:
//  @GetMapping("/countries")
//  public List<CountryJson> getAllCountries() {
//    return grpcGeoClient.getAllCountries();
//  }

}
