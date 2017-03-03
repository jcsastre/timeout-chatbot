package com.timeout.chatbot.block;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeBackBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public WelcomeBackBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        Session session
    ) {
        StringBuilder sbMessage = new StringBuilder();
        if (session.getFbUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + session.getFbUserProfile().getFirstName() + ", welcome back!");
        } else {
            sbMessage.append("Hi, welcome back!");
        }

        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            sbMessage.toString()
        );
    }
}
