package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.payload.PayloadHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandlerImpl implements QuickReplyMessageEventHandler {

    private final SessionPool sessionPool;
    private final PayloadHandler payloadHandler;

    @Autowired
    public QuickReplyMessageEventHandlerImpl(
        SessionPool sessionPool,
        PayloadHandler payloadHandler
    ) {
        this.sessionPool = sessionPool;
        this.payloadHandler = payloadHandler;
    }

    @Override
    public void handle(QuickReplyMessageEvent event) {
        final String payload = event.getQuickReply().getPayload();
        final String recipitientId = event.getRecipient().getId();

        final Session session = this.sessionPool.getSession(
            new PageUid(recipitientId),
            event.getSender().getId()
        );

        payloadHandler.handle(payload, session);
    }
}
