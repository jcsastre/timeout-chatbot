package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.DiscoverBlock;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentDiscoverHandler {

    private final DiscoverBlock discoverBlock;
    private final BlockError blockError;
    private final SessionService sessionService;

    @Autowired
    public IntentDiscoverHandler(
        DiscoverBlock discoverBlock,
        BlockError blockError,
        SessionService sessionService) {
        this.discoverBlock = discoverBlock;
        this.blockError = blockError;
        this.sessionService = sessionService;
    }

    public void handle(Session session) throws MessengerApiException, MessengerIOException {

        switch (session.getSessionState()) {

            case UNDEFINED:
                proceed(session);
                break;

            case SEARCH_SUGGESTIONS:
                proceed(session);
                break;

            case DISCOVER:
                proceed(session);
                break;

            case MOST_LOVED:
                proceed(session);
                break;

            case SEARCHING:
                proceed(session);
                break;

            case ITEM:
                proceed(session);
                break;

            case SUBMITTING_REVIEW:
                handleSubmittingReview(session);
                break;

            case BOOKING:
                handleBooking(session);
                break;

            default:
                blockError.send(session.getUser().getMessengerId());
        }
    }

    private void handleSubmittingReview(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        //TODO: ask before cancelling
        //TODO: ...
    }

    private void handleBooking(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        //TODO: ask before cancelling
        //TODO: return to previous looking context
    }

    private void proceed(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        session.setSessionState(SessionState.DISCOVER);
        sessionService.persistSession(session);

        discoverBlock.send(session.getUser().getMessengerId());
    }
}
