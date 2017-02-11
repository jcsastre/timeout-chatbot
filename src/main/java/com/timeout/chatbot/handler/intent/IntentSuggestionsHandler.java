package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSuggestionsHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public IntentSuggestionsHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void handle(Session session) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
            quickReplyBuilderForCurrentSessionState.build(session)
        );

//        if (session.getSessionState() == SessionState.BOOKING) {
//
//            //TODO: ask before cancelling
//            //TODO: return to previous looking context
//
//            messengerSendClient.sendTextMessage(
//                session.getUser().getMessengerId(),
//                "Cancelling booking"
//            );
//        }
//
//        session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
//        blockService.sendSuggestionsBlock(session);
    }
}
