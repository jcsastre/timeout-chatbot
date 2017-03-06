package com.timeout.chatbot.handler.messenger;

import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.states.DefaultPayloadHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PostbackEventAsyncHandler {

    private final SessionPool sessionPool;
    private final DefaultPayloadHandler defaultPayloadHandler;
    private final BlockError blockError;

    @Autowired
    public PostbackEventAsyncHandler(
        SessionPool sessionPool,
        DefaultPayloadHandler defaultPayloadHandler,
        BlockError blockError
    ) {
        this.sessionPool = sessionPool;
        this.defaultPayloadHandler = defaultPayloadHandler;
        this.blockError = blockError;
    }

    @Async
    public void handle(
        String payload,
        String recipientId,
        String senderId,
        Date timestamp
    ) {
        final Session session = this.sessionPool.getSession(
            new PageUid(recipientId),
            senderId
        );

        boolean proceed = true;
        final Date currentTimestamp = session.getCurrentTimestamp();
        if (currentTimestamp != null) {
            if (currentTimestamp.equals(timestamp)) {
                proceed = false;
            }
        }

        if (proceed) {
            session.setCurrentTimestamp(timestamp);
            try {
                defaultPayloadHandler.handle(payload, session);
            } catch (Exception e) {
                e.printStackTrace();
                blockError.send(session.getUser());
            }
        }
    }
}
