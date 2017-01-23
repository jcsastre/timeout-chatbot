package com.timeout.chatbot.handler.textmessage;

import ai.api.model.Result;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;
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
public class TextMessageEventOnUndefinedStateHandler {

    private final ApiAiService apiAiService;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final IntentFindThingsToDoHandler intentFindThingsToDoHandler;
    private final IntentFindRestaurantsHandler intentFindRestaurantsHandler;

    @Autowired
    public TextMessageEventOnUndefinedStateHandler(
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
        final ApiaiIntent apiaiIntent = ApiaiIntent.fromApiaiAction(apiaiAction);

        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
//        if (apiaiParameters != null) {
//            updateFilterParams(apiaiParameters);
//        }

        SessionUtils.printConsole(apiaiAction, apiaiParameters);

        switch (apiaiIntent) {

            case GREETINGS:
                blockService.sendSuggestionsBlock(session.getUser());
                blockService.sendDiscoverBlock(session.getUser());
                break;

            case SUGGESTIONS:
                blockService.sendSuggestionsBlock(session.getUser());
                break;

            case DISCOVER:
                blockService.sendDiscoverBlock(session.getUser());
                break;

            case FIND_THINGSTODO:
                session.setSessionState(SessionState.LOOKING);
                intentFindThingsToDoHandler.handle(session.getUser().getMessengerId());
                break;

            case FIND_RESTAURANTS:
                session.setSessionState(SessionState.LOOKING);
                intentFindRestaurantsHandler.handle(session);
                break;

            case FIND_RESTAURANTS_NEARBY:
                onIntentFindRestaurantsNearby();
                break;

            case FIND_BARSANDPUBS:
                onIntentFindBars();
                break;

            case FIND_BARSANDPUBS_NEARBY:
                onIntentFindBarsNearby();
                break;

            case FIND_ART:
                onIntentFindArt();
                break;

            case FIND_THEATRE:
                onIntentFindTheatre();
                break;

            case FIND_MUSIC:
                onIntentFindMusic();
                break;

            case FIND_NIGHTLIFE:
                onIntentFindNightlife();
                break;

            case FINDS_FILMS:
                onIntentFindFilms(1);
                break;

            case SET_LOCATION:
                //                fsm.apply(Intent.FIND_CAMPINGS);
                break;

            case UNKOWN:
                //                onIntentUnknown();
                break;

            default:
                messengerSendClientWrapper.sendTextMessage(
                    user.getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }
}
