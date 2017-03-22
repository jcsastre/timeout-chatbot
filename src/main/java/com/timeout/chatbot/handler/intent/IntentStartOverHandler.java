package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import org.springframework.stereotype.Component;

@Component
public class IntentStartOverHandler {

    private final SessionService sessionService;
    private final IntentService intentService;
    private final BlockService blockService;

    public IntentStartOverHandler(
        SessionService sessionService,
        IntentService intentService,
        BlockService blockService
    ) {
        this.sessionService = sessionService;
        this.intentService = intentService;
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

        sessionService.resetSession(session);

        blockService.getWelcomeBack().send(
            session
        );

        intentService.getIntentDiscoverHandler().handle(session);
    }
}
