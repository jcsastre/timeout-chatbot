package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final PostbackEventAsyncHandler postbackEventAsyncHandler;

    public PostbackEventHandlerImpl(
        PostbackEventAsyncHandler postbackEventAsyncHandler
    ) {
        this.postbackEventAsyncHandler = postbackEventAsyncHandler;
    }

    @Override
    public void handle(
        PostbackEvent event
    ) {
        postbackEventAsyncHandler.handleAsync(
            event.getPayload(),
            event.getRecipient().getId(),
            event.getSender().getId()
        );
    }
}
