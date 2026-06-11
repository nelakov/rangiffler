package com.elakov.rangiffler.api.grpc;

import com.elakov.grpc.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.rangiffler.config.services.ServicesProperties;

public class UserdataGrpcClient extends BaseGrpcClient {

    private final RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub userdataServiceBlockingStub;

    public UserdataGrpcClient() {
        super(ServicesProperties.USERDATA_GRPC_HOST, ServicesProperties.USERDATA_GRPC_PORT);
        userdataServiceBlockingStub = RangifflerUserdataServiceGrpc.newBlockingStub(channel);
    }

    public UserArray getAllFriends(String username) {
        return userdataServiceBlockingStub.getAllFriends(getUsername(username));
    }
}
