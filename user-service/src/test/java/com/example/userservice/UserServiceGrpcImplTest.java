package com.example.userservice;

import com.example.grpcproto.UserRequest;
import com.example.grpcproto.UserResponse;
import com.example.grpcproto.UserServiceGrpc;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceGrpcImplTest {

    private Server server;
    private ManagedChannel channel;

    @BeforeEach
    void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new UserServiceGrpcImpl())
                .build()
                .start();
        channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
    }

    @AfterEach
    void tearDown() {
        channel.shutdownNow();
        server.shutdownNow();
    }

    @Test
    void getUserById_returnsExpectedResponse() {
        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        UserRequest request = UserRequest.newBuilder().setUserId("123").build();
        UserResponse response = stub.getUserById(request);
        assertEquals("User ID: 123", response.getResponseMessage());
    }
}