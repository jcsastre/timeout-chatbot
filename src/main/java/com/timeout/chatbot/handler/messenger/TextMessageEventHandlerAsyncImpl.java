package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.handler.states.DefaultTextHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStateTextHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TextMessageEventHandlerAsyncImpl {

    private final SessionService sessionService;
    private final BlockService blockService;
    private final DefaultTextHandler defaultTextHandler;
    private final SubmittingReviewStateTextHandler submittingReviewStateTextHandler;

    @Autowired
    public TextMessageEventHandlerAsyncImpl(
        SessionService sessionService,
        BlockService blockService,
        DefaultTextHandler defaultTextHandler,
        SubmittingReviewStateTextHandler submittingReviewStateTextHandler
    ) {
        this.sessionService = sessionService;
        this.blockService = blockService;
        this.defaultTextHandler = defaultTextHandler;
        this.submittingReviewStateTextHandler = submittingReviewStateTextHandler;
    }

    @Async
    public void handle(
        TextMessageEvent event
    ) {
        final Session session =
            sessionService.getSession(
                event.getRecipient().getId(),
                event.getSender().getId()
            );

        try {
            handleInternal(
                session,
                event.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
            blockService.getError().send(session.user.messengerId);
        }
    }

    private void handleInternal(
        Session session,
        String text
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException, NluException {

        switch (session.state) {

            case SUBMITTING_REVIEW:
                submittingReviewStateTextHandler.handle(text, session);
                break;

            default:
                defaultTextHandler.handle(text, session);
        }
    }
}
