package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final PostbackEventAsyncHandler postbackEventAsyncHandler;

    public QuickReplyMessageEventHandlerImpl(PostbackEventAsyncHandler postbackEventAsyncHandler) {
        this.postbackEventAsyncHandler = postbackEventAsyncHandler;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        postbackEventAsyncHandler.handle(
            event.getQuickReply().getPayload(),
            event.getRecipient().getId(),
            event.getSender().getId(),
            event.getTimestamp()
        );
    }
}
