package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final PayloadAsyncHandler payloadAsyncHandler;

    public QuickReplyMessageEventHandlerImpl(PayloadAsyncHandler payloadAsyncHandler) {
        this.payloadAsyncHandler = payloadAsyncHandler;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        payloadAsyncHandler.handleAsync(
            event.getQuickReply().getPayload(),
            event.getRecipient().getId(),
            event.getSender().getId()
        );
    }
}
