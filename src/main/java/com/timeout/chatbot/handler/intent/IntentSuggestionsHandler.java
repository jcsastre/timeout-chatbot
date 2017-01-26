package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSuggestionsHandler {

    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public IntentSuggestionsHandler(BlockService blockService, MessengerSendClientWrapper messengerSendClientWrapper) {
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(Session session) {

        if (session.getSessionState() == SessionState.BOOKING) {

            //TODO: ask before cancelling
            //TODO: return to previous looking context

            messengerSendClientWrapper.sendTextMessage(
                session.getUser().getMessengerId(),
                "Cancelling booking"
            );

            session.setSessionState(SessionState.LOOKING);
        }

        blockService.sendSuggestionsBlock(session.getUser());
    }
}
