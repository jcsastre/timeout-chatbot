package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.states.DefaultTextHandler;
import com.timeout.chatbot.handler.states.booking.BookingStateTextHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStateTextHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TextMessageEventHandlerAsyncImpl implements TextMessageEventHandler {

    private final SessionService sessionService;
    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClient msc;
    private final DefaultTextHandler defaultTextHandler;
    private final SubmittingReviewStateTextHandler submittingReviewStateTextHandler;
    private final BookingStateTextHandler bookingStateTextHandler;

    @Autowired
    public TextMessageEventHandlerAsyncImpl(
        SessionService sessionService,
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient msc,
        DefaultTextHandler defaultTextHandler,
        SubmittingReviewStateTextHandler submittingReviewStateTextHandler,
        BookingStateTextHandler bookingStateTextHandler
    ) {
        this.sessionService = sessionService;
        this.intentService = intentService;
        this.blockService = blockService;
        this.msc = msc;
        this.defaultTextHandler = defaultTextHandler;
        this.submittingReviewStateTextHandler = submittingReviewStateTextHandler;
        this.bookingStateTextHandler = bookingStateTextHandler;
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

            case BOOKING:
                bookingStateTextHandler.handle(text, session);
                break;

            default:
                defaultTextHandler.handle(text, session);
        }
    }
}
