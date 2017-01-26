package com.timeout.chatbot.handler.messenger;

import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.domain.page.PageUid;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.NluService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionPool;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextMessageEventHandlerImpl implements TextMessageEventHandler {

    private final SessionPool sessionPool;
    private final IntentService intentService;
    private final NluService nluService;
    private final ApiAiService apiAiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public TextMessageEventHandlerImpl(
        SessionPool sessionPool,
        IntentService intentService,
        NluService nluService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.sessionPool = sessionPool;
        this.intentService = intentService;
        this.nluService = nluService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    @Override
    public void handle(
        TextMessageEvent event
    ) {
        final Session session = this.sessionPool.getSession(
            new PageUid(event.getRecipient().getId()),
            event.getSender().getId()
        );

        try {
            processNluResult(
                session,
                nluService.processText(event.getText())
            );
        } catch (NluException e) {
            e.printStackTrace();
            messengerSendClientWrapper.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, I didn't understand you"
            );
        }
    }

    private void processNluResult(
        Session session,
        NluResult nluResult
    ) {
        switch (nluResult.getNluIntentType()) {

            case GREETINGS:
                intentService.handleGreetings(session);
                break;

            case SUGGESTIONS:
                intentService.handleSuggestions(session);
                break;

            case DISCOVER:
                intentService.handleDiscover(session);
                break;

            case FIND_THINGSTODO:
                intentService.handleFindThingsToDo(session);
                break;

            case FIND_RESTAURANTS:
                session.setSessionState(SessionState.LOOKING);
                intentFindRestaurantsHandler.handle(session);
                break;

            // find restaurants
            // find restaurants near me
            // find restaurants nearby
            // find restaurants candem

            case FIND_RESTAURANTS_NEARBY:
                session.setSessionState(SessionState.LOOKING);
                intentFindRestaurantsHandler.handleNearby(session);
                break;

//            case FIND_BARSANDPUBS:
//                onIntentFindBars();
//                break;
//
//            case FIND_BARSANDPUBS_NEARBY:
//                onIntentFindBarsNearby();
//                break;

//            case SET_LOCATION:
//                //TODO
//                break;

            case UNKOWN:
                //TODO: onIntentUnknown();
                break;

            default:
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
