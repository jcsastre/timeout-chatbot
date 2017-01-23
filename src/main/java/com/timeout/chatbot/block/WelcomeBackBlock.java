package com.timeout.chatbot.block;

import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
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
        User user
    ) {
        StringBuilder sbMessage = new StringBuilder();
        if (user.getFbUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + user.getFbUserProfile().getFirstName() + ", welcome back!");
        } else {
            sbMessage.append("Hi, welcome back!");
        }

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            sbMessage.toString()
        );
    }
}
