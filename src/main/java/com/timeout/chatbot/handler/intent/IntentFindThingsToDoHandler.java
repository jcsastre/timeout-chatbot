package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
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
        String userMessengerId
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userMessengerId,
            "Sorry, 'Things to do' is not yet implemented."
        );
    }
}
