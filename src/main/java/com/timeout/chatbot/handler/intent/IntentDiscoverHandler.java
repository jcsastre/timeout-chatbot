package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.stereotype.Component;

@Component
public class IntentDiscoverHandler {

    private final BlockService blockService;

    public IntentDiscoverHandler(
        BlockService blockService
    ) {
        this.blockService = blockService;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        switch (session.state) {

            case BOOKING:
                //TODO: Implement Booking Cancel Flow
                break;

            case SUBMITTING_REVIEW:
                //TODO: Implement Submitting Review Cancel Flow
                break;

            default:
                proceed(session);
        }
    }

    private void proceed(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        session.state = SessionState.DISCOVER;

        blockService.getDiscoverBlock().send(session.user.messengerId);
    }
}
