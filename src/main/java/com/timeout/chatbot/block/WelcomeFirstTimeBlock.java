package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeFirstTimeBlock {

    private final MessengerSendClient messengerSendClient;
    private final SenderActionsHelper senderActionsHelper;

    @Autowired
    public WelcomeFirstTimeBlock(
        MessengerSendClient messengerSendClient,
        SenderActionsHelper senderActionsHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.senderActionsHelper = senderActionsHelper;
    }

    public void send(
        User user
    ) throws MessengerApiException, MessengerIOException {

        final String messengerId = user.getMessengerId();

        msgHi(user, messengerId);

        msgPresentation(user, messengerId);

        msgHelp(user, messengerId);

        /////////////////////

//        messengerSendClient.sendImageAttachment(
//            user.getMessengerId(),
//            "https://im.ezgif.com/tmp/ezgif-1-2c120f0a36.gif"
//        );

//
//
//        messengerSendClient.sendImageAttachment(
//            user.getMessengerId(),
//            "https://media.giphy.com/media/pxwlYSM8PfY5y/giphy.gif"
//        );


//        messengerSendClient.sendTextMessage(
//            user.getMessengerId(),
//            "Let's start!"
//        );

//        messengerSendClient.sendTextMessage(
//            user.getMessengerId(),
//            "First, some examples of questions you can ask me"
//        );

//        suggestionsBlock.send(user.getMessengerId());

//        messengerSendClient.sendTextMessage(
//            user.getMessengerId(),
//            "If the options above doesn't fit your needs, just ask me. " +
//                "And if you don't know what to ask type 'help'"
//        );
    }

    private void msgHelp(User user, String messengerId) throws MessengerApiException, MessengerIOException {
        senderActionsHelper.typingOnAndWait(messengerId, 2000);
        messengerSendClient.sendTextMessage(
            user.getMessengerId(),
            "I can help you to find the most beautiful places and vibrant events in London"
        );
    }

    private void msgPresentation(
        User user,
        String messengerId
    ) throws MessengerApiException, MessengerIOException {

        senderActionsHelper.typingOnAndWait(messengerId, 1500);
        messengerSendClient.sendTextMessage(
            user.getMessengerId(),
            "I'm Brian, the chatbot of Timeout London"
        );

        senderActionsHelper.typingOn(messengerId);
        messengerSendClient.sendImageAttachment(
            user.getMessengerId(),
            "https://media.giphy.com/media/QGMiTNBw8hB72/giphy.gif"
        );
    }

    private void msgHi(User user, String messengerId) throws MessengerApiException, MessengerIOException {
        String msg = null;
        if (user.getFbUserProfile().getFirstName() != null) {
            msg = "Hi " + user.getFbUserProfile().getFirstName() + "!";
        } else {
            msg = "Hi!";
        }

        senderActionsHelper.typingOnAndWait(messengerId, 300);
        messengerSendClient.sendTextMessage(
            user.getMessengerId(),
            msg
        );
    }
}
