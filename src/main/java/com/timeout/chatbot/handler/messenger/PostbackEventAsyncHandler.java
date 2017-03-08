package com.timeout.chatbot.handler.messenger;

import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.states.DefaultPayloadHandler;
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
    void handleAsync(
        String payload,
        String recipientId,
        String senderId
    ) {
        this.sessionPool.getSession(
            new PageUid(recipientId),
            senderId
        ).addCallback(
            (session -> {
                final Date currentTimestamp = session.getCurrentTimestamp();
                session.setCurrentTimestamp(currentTimestamp);
                try {
                    defaultPayloadHandler.handle(payload, session);
                } catch (Exception e) {
                    e.printStackTrace();
                    blockError.send(session.getUser());
                }

            }),
            Throwable::printStackTrace
        );

//        listenableFuture.addCallback(
//            new ListenableFutureCallback<Session>() {
//                @Override
//                public void onSuccess(Session session) {
//                }
//                @Override
//                public void onFailure(Throwable ex) {
//                }
//            }
//        );
    }
}
