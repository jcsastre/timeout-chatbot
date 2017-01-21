package com.timeout.chatbot.session.exploring;

import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;

import java.util.Map;

public class ExploringContext {

    public void applyUtterance(
        String utterance
    ) {

        final String apiaiAction = apiaiResult.getAction();
        final ApiaiIntent apiaiIntent = ApiaiIntent.fromApiaiAction(apiaiAction);

        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
        if (apiaiParameters != null) {
            updateFilterParams(apiaiParameters);
        }

        printConsole(apiaiAction, apiaiParameters);

        switch (apiaiIntent) {
            case GREETINGS:
                onIntentGreetings();
                break;
            case SUGGESTIONS:
                onIntentSuggestions();
                break;
            case FIND_THINGSTODO:
                onIntentFindThingstodo();
                break;
            case FIND_RESTAURANTS:
                onIntentFindRestaurants();
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
            case DISCOVER:
                onIntentDiscover();
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
