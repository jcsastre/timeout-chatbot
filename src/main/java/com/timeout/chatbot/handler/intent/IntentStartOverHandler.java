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
                //TODO: EIIIIII
                break;

            case SUBMITTING_REVIEW:
                //TODO: EIIIIII
                break;

            default:
                proceed(session);
        }
    }

    private void proceed(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        sessionService.resetSession(session);

        blockService.getWelcomeBackBlock().send(
            session
        );
        
// TODO cambiar a intent discover
//        blockService.getDiscoverBlock().send(
//            session.user.messengerId
//        );
//
//        session.state = SessionState.DISCOVER;
    }
}
