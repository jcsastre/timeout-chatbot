package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.FbUserProfile;
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
        String userMessengerId,
        FbUserProfile fbUserProfile
    ) throws MessengerApiException, MessengerIOException {

        msgHi(userMessengerId, fbUserProfile);

        msgPresentation(userMessengerId);

        msgHelp(userMessengerId);

        /////////////////////

//        messengerSendClient.sendImageAttachment(
//            user.messengerId,
//            "https://im.ezgif.com/tmp/ezgif-1-2c120f0a36.gif"
//        );

//
//
//        messengerSendClient.sendImageAttachment(
//            user.messengerId,
//            "https://media.giphy.com/media/pxwlYSM8PfY5y/giphy.gif"
//        );


//        messengerSendClient.sendTextMessage(
//            user.messengerId,
//            "Let's start!"
//        );

//        messengerSendClient.sendTextMessage(
//            user.messengerId,
//            "First, some examples of questions you can ask me"
//        );

//        suggestionsBlock.send(user.messengerId);

//        messengerSendClient.sendTextMessage(
//            user.messengerId,
//            "If the options above doesn't fit your needs, just ask me. " +
//                "And if you don't know what to ask type 'help'"
//        );
    }

    private void msgHelp(
        String userMessengerId
    ) throws MessengerApiException, MessengerIOException {

        senderActionsHelper.typingOnAndWait(userMessengerId, 2000);
        messengerSendClient.sendTextMessage(
            userMessengerId,
            "I can help you to perform the most beautiful places and vibrant events in London"
        );
    }

    private void msgPresentation(
        String userMessengerId
    ) throws MessengerApiException, MessengerIOException {

        senderActionsHelper.typingOnAndWait(userMessengerId, 1500);
        messengerSendClient.sendTextMessage(
            userMessengerId,
            "I'm Brian, the chatbot of Timeout London"
        );

        senderActionsHelper.typingOn(userMessengerId);
        messengerSendClient.sendImageAttachment(
            userMessengerId,
            "https://media.giphy.com/media/QGMiTNBw8hB72/giphy.gif"
        );
    }

    private void msgHi(
        String userMessengerId,
        FbUserProfile fbUserProfile
    ) throws MessengerApiException, MessengerIOException {

        String msg = null;

        if (fbUserProfile.getFirstName() != null) {
            msg = "Hi " + fbUserProfile.getFirstName() + "!";
        } else {
            msg = "Hi!";
        }

        senderActionsHelper.typingOnAndWait(userMessengerId, 300);
        messengerSendClient.sendTextMessage(
            userMessengerId,
            msg
        );
    }
}
