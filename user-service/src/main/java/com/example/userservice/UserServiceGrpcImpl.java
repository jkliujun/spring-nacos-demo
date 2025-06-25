package com.example.userservice;

import com.example.grpcproto.UserRequest;
import com.example.grpcproto.UserResponse;
import com.example.grpcproto.UserServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

  @Override
  public void getUserById(UserRequest request, StreamObserver<UserResponse> responseObserver) {
      UserResponse response = UserResponse.newBuilder()
              .setResponseMessage("User ID: " + request.getUserId())
              .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
  }
}
