package com.timeout.chatbot.handler;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public PostbackEventHandlerImpl(
        SessionPool sessionPool
    ) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(PostbackEvent event) {
        sessionPool.getSession(
            event.getRecipient().getId(),
            event.getSender().getId()
        ).handlePostbackEvent(event);
    }
}
