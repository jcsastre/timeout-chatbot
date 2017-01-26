package com.timeout.chatbot.handler.payload;

import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.text.OnUndefinedStateTextHandler;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.context.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayloadHandler {

    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final OnUndefinedStateTextHandler onUndefinedStateTextHandler;

    @Autowired
    public PayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        OnUndefinedStateTextHandler onUndefinedStateTextHandler
    ) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.onUndefinedStateTextHandler = onUndefinedStateTextHandler;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) {
        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                blockService.sendWelcomeFirstTimeBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

            case help:
                intentService.handleHelp(session);
                break;

            case suggestions:
                intentService.handleSuggestions(session);
                break;

            case discover:
                intentService.handleDiscover(session);
                break;

            case whats_new:
                intentService.handleWhatsnew(session);
                break;

            case start_over:
                blockService.sendWelcomeBackBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

//            case utterance:
//                final String utterance = payload.getString("utterance");
//                //TODO:
//                //onUndefinedStateTextHandler.handle(utterance, session);
//                break;

            default:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
