package com.example.serverservice;

import com.example.MessageRequest;
import com.example.MessageResponse;
import com.example.MessageServiceGrpc;

import com.example.controller.SseController;
import com.example.entity.ChatEntity;
import com.example.entity.UserEntity;
import com.example.repository.ChatRepository;
import com.example.repository.UserRepository;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@GrpcService

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    @Inject
    private UserRepository userRepository;
    @Inject
    private ChatRepository chatRepository;
    @Inject
    private SseController sseController;
    @Override
    public void message(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setMessage(request.getMessage());
        Optional<UserEntity> userEntity = userRepository.findByUserName(request.getUserName());
        if (userEntity.isPresent() ){
            chatEntity.setUserEntity(userEntity.get());
            chatRepository.save(chatEntity);
            sseController.publishMessage(chatEntity);
        }
        try {
            MessageResponse messageResponse=MessageResponse.newBuilder()
                    .build();
            responseObserver.onNext(messageResponse);
            responseObserver.onCompleted();

        }
        catch (Exception e){
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to send message")
                    .asRuntimeException());
        }

    }
}
