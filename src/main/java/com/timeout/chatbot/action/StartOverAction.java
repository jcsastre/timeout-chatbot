package com.timeout.chatbot.action;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.DiscoverBlock;
import com.timeout.chatbot.block.WelcomeBackBlock;
import com.timeout.chatbot.services.SessionService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.stereotype.Component;

@Component
public class StartOverAction {

    private final SessionService sessionService;
    private final WelcomeBackBlock welcomeBackBlock;
    private final DiscoverBlock discoverBlock;

    public StartOverAction(
        SessionService sessionService,
        WelcomeBackBlock welcomeBackBlock,
        DiscoverBlock discoverBlock
    ) {
        this.sessionService = sessionService;
        this.welcomeBackBlock = welcomeBackBlock;
        this.discoverBlock = discoverBlock;
    }

    public void perform(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        sessionService.resetSession(session);

        welcomeBackBlock.send(session);

        session.state = SessionState.DISCOVER;
        discoverBlock.send(session.user.messengerId);
    }
}
