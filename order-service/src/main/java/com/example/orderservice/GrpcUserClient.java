package com.example.orderservice;

import org.springframework.stereotype.Service;

import com.example.grpcproto.UserRequest;
import com.example.grpcproto.UserResponse;
import com.example.grpcproto.UserServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class GrpcUserClient {

    @GrpcClient("grpc-user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    public String getUser(String userId) {
        UserRequest request = UserRequest.newBuilder().setUserId(userId).build();
        UserResponse response = userServiceStub.getUserById(request);
        return response.getResponseMessage();
    }
}
