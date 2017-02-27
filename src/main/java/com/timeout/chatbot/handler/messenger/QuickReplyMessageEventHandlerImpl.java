package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final PostbackEventHandlerImpl postbackEventHandler;

    @Autowired
    public QuickReplyMessageEventHandlerImpl(
        PostbackEventHandlerImpl postbackEventHandler
    ) {
        this.postbackEventHandler = postbackEventHandler;
    }

    @Override
    public void handle(
        QuickReplyMessageEvent event
    ) {
        postbackEventHandler.handle(
            event.getQuickReply().getPayload(),
            event.getRecipient().getId(),
            event.getSender().getId(),
            event.getTimestamp()
        );
    }
}
