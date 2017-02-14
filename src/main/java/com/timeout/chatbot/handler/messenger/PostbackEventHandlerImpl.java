package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.states.DefaultPayloadHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewPayloadHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final SessionPool sessionPool;
    private final DefaultPayloadHandler defaultPayloadHandler;
    private final SubmittingReviewPayloadHandler submittingReviewPayloadHandler;
    private final ErrorBlock errorBlock;

    @Autowired
    public PostbackEventHandlerImpl(
        SessionPool sessionPool,
        DefaultPayloadHandler defaultPayloadHandler,
        SubmittingReviewPayloadHandler submittingReviewPayloadHandler,
        ErrorBlock errorBlock
    ) {
        this.sessionPool = sessionPool;
        this.defaultPayloadHandler = defaultPayloadHandler;
        this.submittingReviewPayloadHandler = submittingReviewPayloadHandler;
        this.errorBlock = errorBlock;
    }

    @Override
    public void handle(PostbackEvent event) {

        final String payload = event.getPayload();
        final String recipitientId = event.getRecipient().getId();
        final String senderId = event.getSender().getId();

        handle(payload, recipitientId, senderId);
    }

    public void handle(String payload, String recipitientId, String senderId) {

        final Session session = this.sessionPool.getSession(
            new PageUid(recipitientId),
            senderId
        );

        final SessionState sessionState = session.getSessionState();

        try {
            switch (sessionState) {

                case SUBMITTING_REVIEW:
                    submittingReviewPayloadHandler.handle(payload, session);
                    break;

                default:
                    defaultPayloadHandler.handle(payload, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorBlock.send(session.getUser());
        }
    }
}
