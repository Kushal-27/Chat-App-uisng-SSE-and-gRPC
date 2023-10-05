package com.example.serverservice;

import com.example.UserReply;
import com.example.UserRequest;
import com.example.UserServiceGrpc;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.security.BcryptEncoderService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    @Inject
    private UserRepository userRepository;
    @Inject
    private BcryptEncoderService bcryptEncoderService;
    @Override
    public void register( UserRequest request, StreamObserver<UserReply> responseObserver) {
        try {
            UserReply userReply = UserReply.newBuilder()
                    .setUserName(request.getUserName())
                    .setEmail(request.getEmail())
                    .setPhoneNumber(request.getPhoneNumber())
                    .setRole("ROLE_USER")
                    .build();

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(request.getEmail());
            userEntity.setUserName(request.getUserName());
            userEntity.setPhoneNumber(request.getPhoneNumber());
            userEntity.setRole("ROLE_USER");
            userEntity.setPassword(bcryptEncoderService.hashPassword(request.getPassword()));
            userRepository.save(userEntity);
            responseObserver.onNext(userReply);
            responseObserver.onCompleted();
            System.out.println(userReply);
        }
        catch(Exception e){
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());

        }


    }
}
