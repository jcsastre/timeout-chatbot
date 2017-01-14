package com.timeout.chatbot.block;

import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeFirstTimeBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public WelcomeFirstTimeBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        User user
    ) {
        StringBuilder sbMessage = new StringBuilder();
        if (user.getFbUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + user.getFbUserProfile().getFirstName() + "!");
        } else {
            sbMessage.append("Hi!");
        }
        sbMessage.append("\n\n");
        sbMessage.append("I'm Brian, I work as chatbot on Timeout London.");
        sbMessage.append("\n\n");
        sbMessage.append("I know every corner in London, just ask me.");

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            sbMessage.toString()
        );
    }
}
