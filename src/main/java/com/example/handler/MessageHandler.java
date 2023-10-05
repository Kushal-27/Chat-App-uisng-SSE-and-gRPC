package com.example.handler;

import com.example.entity.ChatEntity;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
@Singleton
public class MessageHandler {
    private final Sinks.Many<ChatEntity> chatEntityManySink;

    public MessageHandler() {
        this.chatEntityManySink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void subscribe(Consumer<ChatEntity> listener) {
        chatEntityManySink.asFlux().subscribe(listener);
    }


    public void publish(ChatEntity chatEntity) {
        chatEntityManySink.tryEmitNext(chatEntity);
    }

    public Flux<ChatEntity> subscribeChat() {
        return chatEntityManySink.asFlux();
    }
}
