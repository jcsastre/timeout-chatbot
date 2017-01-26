package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.context.SessionState;
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
        final String recipitientId = event.getRecipient().getId();

        final Session session = this.sessionPool.getSession(
            new PageUid(recipitientId),
            event.getSender().getId()
        );

        final SessionState sessionState = session.getSessionState();

        //TODO
    }
}
