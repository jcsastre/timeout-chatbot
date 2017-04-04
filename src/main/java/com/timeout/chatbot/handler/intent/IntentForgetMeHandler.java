package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentForgetMeHandler {

    private final SenderActionsHelper senderActionsHelper;
    private final UserRepository userRepository;
    private final MessengerSendClient msc;

    @Autowired
    public IntentForgetMeHandler(
        SenderActionsHelper senderActionsHelper, UserRepository userRepository,
        MessengerSendClient msc
    ) {
        this.senderActionsHelper = senderActionsHelper;
        this.userRepository = userRepository;
        this.msc = msc;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final User user = session.user;
        userRepository.delete(user);

        final String messengerId = session.user.messengerId;

        msc.sendImageAttachment(
            messengerId,
            "https://media.giphy.com/media/6IPNUgkpCsDRK/giphy.gif"
        );

        senderActionsHelper.typingOnAndWait(messengerId, 1500);
        msc.sendTextMessage(
            user.messengerId,
            "Hey! You know, your face looks familiar"
        );
    }
}
