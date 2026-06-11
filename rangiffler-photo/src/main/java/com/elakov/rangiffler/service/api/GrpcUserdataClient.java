package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.grpc.rangiffler.grpc.Username;
import org.springframework.stereotype.Component;

@Component
public class GrpcUserdataClient {

    private final RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;

    public GrpcUserdataClient(RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub userdataBlockingStub) {
        this.rangifflerUserdataServiceBlockingStub = userdataBlockingStub;
    }

    public UserArray friends(Username usernameRequest) {
        return rangifflerUserdataServiceBlockingStub.getAllFriends(usernameRequest);
    }
}
