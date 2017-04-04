package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.DiscoverBlock;
import com.timeout.chatbot.block.WelcomeFirstTimeBlock;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentGetStartedHandler {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final DiscoverBlock discoverBlock;

    @Autowired
    public IntentGetStartedHandler(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        DiscoverBlock discoverBlock
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.discoverBlock = discoverBlock;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        welcomeFirstTimeBlock.send(
            session.user.messengerId,
            session.fbUserProfile
        );

        session.state = SessionState.DISCOVER;
        discoverBlock.send(session.user.messengerId);
    }
}
