package com.example.serverservice;

import com.example.interceptors.MessageServiceInterceptor;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Requires(beans = MessageServiceInterceptor.class)

public class GrpcServer implements BeanCreatedEventListener<ServerBuilder<?>> {
    @Inject
    private final MessageServiceImpl messageService;
    @Inject
    private final MessageServiceInterceptor messageServiceInterceptor;
    @Inject
    private final LoginServiceImpl loginService;

    @Inject
    public GrpcServer(MessageServiceInterceptor messageServiceInterceptor, MessageServiceImpl messageService, LoginServiceImpl loginService){
        this.messageService=messageService;
        this.messageServiceInterceptor=messageServiceInterceptor;
        this.loginService=loginService;
    }
    @Override
    public ServerBuilder<?> onCreated(@NonNull BeanCreatedEvent<ServerBuilder<?>> event) {
        return event.getBean()
                .addService(ServerInterceptors.intercept(messageService,messageServiceInterceptor));




    }
}
