package com.timeout.chatbot.handler;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMessageEventHandlerImpl implements AttachmentMessageEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public AttachmentMessageEventHandlerImpl(
        SessionPool sessionPool
    ) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(AttachmentMessageEvent event) {
        sessionPool.getSession(
            event.getRecipient().getId(),
            event.getSender().getId()
        ).handleAttachmentMessageEvent(event);
    }
}
