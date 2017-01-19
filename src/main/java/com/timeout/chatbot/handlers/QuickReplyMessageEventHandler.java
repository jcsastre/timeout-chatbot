package com.timeout.chatbot.handlers;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuickReplyMessageEventHandler implements com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler {
    private final SessionPool sessionPool;

    @Autowired
    public QuickReplyMessageEventHandler(SessionPool sessionPool) {
        this.sessionPool = sessionPool;
    }

    @Override
    public void handle(QuickReplyMessageEvent event) {
        final Session session =
            sessionPool.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        try {
            session.applyPayload(event.getQuickReply().getPayload());
        } catch(Exception e) {
            session.sendTextMessage("Lo siento ha ocurrido un error.");
        }
    }
}
