package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.grpc.rangiffler.grpc.Username;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GrpcUserdataClient {
    @GrpcClient("grpcUserdataClient")
    private RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;

    public UserArray friends(Username usernameRequest) {
        return rangifflerUserdataServiceBlockingStub.getAllFriends(usernameRequest);
    }
}
