package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandler implements com.github.messenger4j.receive.handlers.PostbackEventHandler {

    private final SessionPool sessionPool;

    @Autowired
    public PostbackEventHandler(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(PostbackEvent event) {
        final Session session =
            sessionPool.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        session.applyPayloadAsJsonString(event.getPayload());
    }
}
