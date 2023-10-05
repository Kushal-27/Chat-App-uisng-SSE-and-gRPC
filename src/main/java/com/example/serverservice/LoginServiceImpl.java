package com.example.serverservice;


import com.example.LoginRequest;
import com.example.LoginResponse;
import com.example.LoginServiceGrpc;
import com.example.security.AuthenticateService;
import com.example.security.TokenHandler;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.SQLOutput;

@GrpcService
public class LoginServiceImpl extends LoginServiceGrpc.LoginServiceImplBase {
    @Inject
    private TokenHandler tokenHandler;
    @Inject
    private AuthenticateService authenticateService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {

        try {
            String token = tokenHandler.generateAccessToken(request);
            if (authenticateService.authenticateUser(request)) {
                LoginResponse loginResponse = LoginResponse.newBuilder()
                        .setAccessToken(token)
                        .setUserName(request.getUserName())
                        .build();
                responseObserver.onNext(loginResponse);
                responseObserver.onCompleted();
                System.out.println("completed");

            }
            else {
                responseObserver.onError(Status.INTERNAL
                        .withDescription("Authentication Failed")
                        .asRuntimeException());
            }
        }
        catch (Exception e){
            System.out.println(e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Login failed")
                    .asRuntimeException());
        }
    }
}
