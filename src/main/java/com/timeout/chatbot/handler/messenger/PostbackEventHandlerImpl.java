package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.states.DefaultPayloadHandler;
import com.timeout.chatbot.handler.states.booking.BookingStatePayloadHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStatePayloadHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostbackEventHandlerImpl implements PostbackEventHandler {

    private final SessionPool sessionPool;
    private final DefaultPayloadHandler defaultPayloadHandler;
    private final SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler;
    private final BookingStatePayloadHandler bookingStatePayloadHandler;
    private final BlockError blockError;

    @Autowired
    public PostbackEventHandlerImpl(
        SessionPool sessionPool,
        DefaultPayloadHandler defaultPayloadHandler,
        SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler,
        BookingStatePayloadHandler bookingStatePayloadHandler,
        BlockError blockError
    ) {
        this.sessionPool = sessionPool;
        this.defaultPayloadHandler = defaultPayloadHandler;
        this.submittingReviewStatePayloadHandler = submittingReviewStatePayloadHandler;
        this.bookingStatePayloadHandler = bookingStatePayloadHandler;
        this.blockError = blockError;
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
                    submittingReviewStatePayloadHandler.handle(payload, session);
                    break;

                case BOOKING:
                    bookingStatePayloadHandler.handle(payload, session);
                    break;

                default:
                    defaultPayloadHandler.handle(payload, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            blockError.send(session.getUser());
        }
    }
}
