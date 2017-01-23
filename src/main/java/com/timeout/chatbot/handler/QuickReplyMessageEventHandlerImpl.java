package com.timeout.chatbot.handler;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public QuickReplyMessageEventHandlerImpl(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(QuickReplyMessageEvent event) {
        sessionPool.getSession(
            event.getRecipient().getId(),
            event.getSender().getId()
        ).handleQuickReplyMessageEvent(event);
    }
}
