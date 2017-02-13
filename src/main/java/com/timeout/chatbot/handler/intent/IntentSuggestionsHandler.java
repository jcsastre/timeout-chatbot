package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSuggestionsHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public IntentSuggestionsHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
    }

    public void handle(Session session) throws MessengerApiException, MessengerIOException {

        if (session.getSessionState() == SessionState.BOOKING) {

            //TODO: ask before cancelling
            //TODO: return to previous looking context

            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Cancelling booking"
            );
        }

        session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
        blockService.sendSuggestionsBlock(session);
    }
}
