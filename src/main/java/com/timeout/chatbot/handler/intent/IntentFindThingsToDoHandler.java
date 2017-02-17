package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentFindThingsToDoHandler {

    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public IntentFindThingsToDoHandler(
        MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.messengerSendClient = messengerSendClient;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            "Sorry, 'Things to do' is not implemented yet",
            quickReplyBuilderForCurrentSessionState.build(session)
        );
    }
}
