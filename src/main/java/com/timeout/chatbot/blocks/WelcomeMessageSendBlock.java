package com.timeout.chatbot.blocks;

import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeMessageSendBlock {

    private final GraffittiService graffittiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public WelcomeMessageSendBlock(
        GraffittiService graffittiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.graffittiService = graffittiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(User user) {
        StringBuilder sbMessage = new StringBuilder();
        if (user.getFbUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + user.getFbUserProfile().getFirstName() + "!");
        } else {
            sbMessage.append("Hi!");
        }
        sbMessage.append("\n\n");
        sbMessage.append("I'm Julio, I work as chatbot on Timeout London.");
        sbMessage.append("\n\n");
        sbMessage.append("I know every corner in London, just ask me.");

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            sbMessage.toString()
        );
    }
}
