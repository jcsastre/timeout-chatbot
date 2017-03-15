package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final PayloadAsyncHandler payloadAsyncHandler;

    public PostbackEventHandlerImpl(
        PayloadAsyncHandler payloadAsyncHandler
    ) {
        this.payloadAsyncHandler = payloadAsyncHandler;
    }

    @Override
    public void handle(
        PostbackEvent event
    ) {
        payloadAsyncHandler.handleAsync(
            event.getPayload(),
            event.getRecipient().getId(),
            event.getSender().getId()
        );
    }
}
