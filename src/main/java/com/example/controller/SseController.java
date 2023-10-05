package com.example.controller;

import com.example.entity.ChatEntity;
import com.example.handler.MessageHandler;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.cors.CrossOrigin;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Controller

public class SseController {
    private final MessageHandler messageHandler;
    private final Sinks.Many<ChatEntity> chatEntityMany;

    public SseController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.chatEntityMany = Sinks.many().multicast().onBackpressureBuffer();
        messageHandler.subscribe(chatEntityMany::tryEmitNext);
    }


    @Produces(MediaType.APPLICATION_JSON)
    public Mono<ChatEntity> publishMessage(@Valid @Body ChatEntity chatEntity) {
        messageHandler.publish(chatEntity);
        return Mono.just(chatEntity);
    }

    @Get("/live")
    @Produces(MediaType.TEXT_EVENT_STREAM)
    @ExecuteOn(TaskExecutors.IO)
    public Flux<ChatEntity> subscribeChat() {
        return chatEntityMany.asFlux();
    }
}
