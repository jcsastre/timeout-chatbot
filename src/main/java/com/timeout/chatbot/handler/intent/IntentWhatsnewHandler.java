package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentWhatsnewHandler {

    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public IntentWhatsnewHandler(MessengerSendClientWrapper messengerSendClientWrapper) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(Session session) {
        messengerSendClientWrapper.sendTextMessage(
            session.user.messengerId,
            "Sorry, 'GraffittiFacetV4Where's New' is not implemented"
        );
    }
}
