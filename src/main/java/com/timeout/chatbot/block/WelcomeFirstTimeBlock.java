package com.timeout.chatbot.block;

import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeFirstTimeBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final SuggestionsBlock suggestionsBlock;

    @Autowired
    public WelcomeFirstTimeBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        SuggestionsBlock suggestionsBlock
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.suggestionsBlock = suggestionsBlock;
    }

    public void send(
        User user
    ) {
        String msg = null;
        StringBuilder sbMessage = new StringBuilder();
        if (user.getFbUserProfile().getFirstName() != null) {
            msg = "Hi " + user.getFbUserProfile().getFirstName() + "!";
        } else {
            msg = "Hi!";
        }
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            msg
        );

//        messengerSendClientWrapper.sendImageAttachment(
//            user.getMessengerId(),
//            "https://media.giphy.com/media/pxwlYSM8PfY5y/giphy.gif"
//        );

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "I'm Brian, I work as chatbot for Timeout London"
        );

//        messengerSendClientWrapper.sendImageAttachment(
//            user.getMessengerId(),
//            "https://media.giphy.com/media/QGMiTNBw8hB72/giphy.gif"
//        );

//        messengerSendClientWrapper.sendTextMessage(
//            user.getMessengerId(),
//            "Let's start!"
//        );

//        messengerSendClientWrapper.sendTextMessage(
//            user.getMessengerId(),
//            "First, some examples of questions you can ask me"
//        );

//        suggestionsBlock.send(user.getMessengerId());

//        messengerSendClientWrapper.sendTextMessage(
//            user.getMessengerId(),
//            "If the options above doesn't fit your needs, just ask me. " +
//                "And if you don't know what to ask type 'help'"
//        );
    }
}
