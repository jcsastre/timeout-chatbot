package com.timeout.chatbot.platforms.messenger.receiver.handlers;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.timeout.chatbot.domain.session.Session;
import com.timeout.chatbot.domain.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandler implements com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler {
    @Autowired
    private SessionPool sessionPool;

    @Override
    public void handle(QuickReplyMessageEvent event) {

        final String pageId = event.getSender().getId();
        final String recipientId = event.getSender().getId();
        final String payload = event.getQuickReply().getPayload();

        final Session session = sessionPool.getSession(pageId, recipientId);
        session.applyUtterance(payload);
    }
}
