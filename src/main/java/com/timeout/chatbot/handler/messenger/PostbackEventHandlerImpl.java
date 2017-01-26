package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.payload.PayloadHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final SessionPool sessionPool;
    private final PayloadHandler payloadHandler;

    @Autowired
    public PostbackEventHandlerImpl(
        SessionPool sessionPool,
        PayloadHandler payloadHandler
    ) {
        this.sessionPool = sessionPool;
        this.payloadHandler = payloadHandler;
    }

    @Override
    public void handle(PostbackEvent event) {
        final String payload = event.getPayload();

        final String recipitientId = event.getRecipient().getId();

        final Session session = this.sessionPool.getSession(
            new PageUid(recipitientId),
            event.getSender().getId()
        );

        payloadHandler.handle(payload, session);
    }
}
