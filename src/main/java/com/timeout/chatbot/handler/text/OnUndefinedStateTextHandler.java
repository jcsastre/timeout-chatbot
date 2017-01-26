package com.timeout.chatbot.handler.text;

import ai.api.model.Result;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.nlu.intent.NluIntentType;
import com.timeout.chatbot.handler.intent.IntentFindRestaurantsHandler;
import com.timeout.chatbot.handler.intent.IntentFindThingsToDoHandler;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionUtils;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OnUndefinedStateTextHandler {

    private final ApiAiService apiAiService;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final IntentFindThingsToDoHandler intentFindThingsToDoHandler;
    private final IntentFindRestaurantsHandler intentFindRestaurantsHandler;

    @Autowired
    public OnUndefinedStateTextHandler(
        ApiAiService apiAiService,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper, IntentFindThingsToDoHandler intentFindThingsToDoHandler, IntentFindRestaurantsHandler intentFindRestaurantsHandler) {
        this.apiAiService = apiAiService;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.intentFindThingsToDoHandler = intentFindThingsToDoHandler;
        this.intentFindRestaurantsHandler = intentFindRestaurantsHandler;
    }

    public void handle(
        String text,
        Session session
    ) {
        blockService.sendWelcomeBackBlock(session.getUser());

        final Result apiaiResult = apiAiService.getApiaiResult(text);
        if (apiaiResult != null) {
            handleApiResult(apiaiResult, session);
        } else {
            blockService.sendSuggestionsBlock(session.getUser());
            blockService.sendDiscoverBlock(session.getUser());
        }
    }

    private void handleApiResult(
        Result apiaiResult,
        Session session
    ) {
        final String apiaiAction = apiaiResult.getAction();
        final NluIntentType nluIntentType = NluIntentType.fromApiaiAction(apiaiAction);

        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
        SessionUtils.printConsole(apiaiAction, apiaiParameters);

        switch (nluIntentType) {

            case FIND_RESTAURANTS:
                session.setSessionState(SessionState.LOOKING);
                intentFindRestaurantsHandler.handle(session);
                break;

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
