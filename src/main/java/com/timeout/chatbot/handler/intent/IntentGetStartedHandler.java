package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.WelcomeFirstTimeBlock;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentGetStartedHandler {

    private final WelcomeFirstTimeBlock welcomeFirstTimeBlock;
    private final IntentDiscoverHandler intentDiscoverHandler;

    @Autowired
    public IntentGetStartedHandler(
        WelcomeFirstTimeBlock welcomeFirstTimeBlock,
        IntentDiscoverHandler intentDiscoverHandler
    ) {
        this.welcomeFirstTimeBlock = welcomeFirstTimeBlock;
        this.intentDiscoverHandler = intentDiscoverHandler;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        welcomeFirstTimeBlock.send(session.getUser());
        intentDiscoverHandler.handle(session);
    }
}
