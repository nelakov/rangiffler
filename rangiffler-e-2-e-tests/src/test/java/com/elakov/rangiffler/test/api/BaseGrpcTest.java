package com.elakov.rangiffler.test.api;

import com.elakov.rangiffler.api.grpc.CountryGrpcClient;
import com.elakov.rangiffler.api.grpc.PhotoGrpcClient;
import com.elakov.rangiffler.data.repository.photo.PhotoRepository;
import com.elakov.rangiffler.data.repository.photo.PhotoRepositoryImpl;
import com.elakov.rangiffler.jupiter.annotation.meta.GrpcTest;

@GrpcTest
public class BaseGrpcTest {

    protected CountryGrpcClient countryGrpcClient = new CountryGrpcClient();
    protected PhotoGrpcClient photoGrpcClient = new PhotoGrpcClient();
    protected PhotoRepository photoRepository = new PhotoRepositoryImpl();

}
