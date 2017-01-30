package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentFindThingsToDoHandler {

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public IntentFindThingsToDoHandler(MessengerSendClientWrapper messengerSendClientWrapper) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(
        Session session
    ) {
        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            "Sorry, 'Things to do' is not yet implemented."
        );
    }
}