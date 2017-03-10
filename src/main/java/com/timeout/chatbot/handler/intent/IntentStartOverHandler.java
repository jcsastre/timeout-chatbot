package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.WelcomeBackBlock;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentStartOverHandler {

    private final SessionService sessionService;
    private final WelcomeBackBlock welcomeBackBlock;
    private final IntentDiscoverHandler intentDiscoverHandler;

    @Autowired
    public IntentStartOverHandler(
        SessionService sessionService,
        WelcomeBackBlock welcomeBackBlock,
        IntentDiscoverHandler intentDiscoverHandler
    ) {
        this.sessionService = sessionService;
        this.welcomeBackBlock = welcomeBackBlock;
        this.intentDiscoverHandler = intentDiscoverHandler;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        sessionService.resetSession(session);
        session.state = SessionState.UNDEFINED;
        session.stateSearchingBag = null;
        session.stateItemBag = null;
        session.stateBookingBag = null;
        session.stateSubmittingReviewBag = null;

        welcomeBackBlock.send(session);
        intentDiscoverHandler.handle(session);
    }
}
