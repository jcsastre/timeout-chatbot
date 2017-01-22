package com.timeout.chatbot.session.context;

import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;

import java.util.Map;

public class LookingSessionContext implements SessionContext {



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

    @Override
    public void applyPayload(String payloadAsJsonString) {

        final JSONObject payloadAsJson = new JSONObject(payloadAsJsonString);

        try {
            PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));
            switch (payloadType) {
                case get_started:
                    // userRepository.deleteAll(); //TODO: delte!!!
                    userRepository.save(user);
                    blockService.sendWelcomeFirstTimeBlock(user);
                    blockService.sendSuggestionsBlock(user);
                    sendMySuggestionsDoesnot();
                    break;
                case utterance:
                    final String utterance = payloadAsJson.getString("utterance");
                    applyUtterance(utterance);
                    break;
                case venues_get_a_summary:
                    blockService.sendVenueSummaryBlock(
                        user.getMessengerId(),
                        payloadAsJson.getString("venue_id")
                    );
                    break;
                case venues_see_more:
                    if (sessionContext == SessionContext.EXPLORING_BARS) {
                        onIntentFindBars();
                    } else if (sessionContext == SessionContext.EXPLORING_RESTAURANTS) {
                        onIntentFindRestaurants();
                    }
                    break;
                case set_location:
                    blockService.sendGeolocationAskBlock(user.getMessengerId());
                    break;
                case restaurants_set_cuisine:
                    sendTextMessage("Sorry, set cuisine is not yet implemented.");
                    //TODO: restaurants_set_cuisine
                    break;
                case films_more_info:
                    sendTextMessage("Sorry, 'More info' is not yet implemented.");
                    //TODO: films_more_info
                    break;
                case films_find_cinemas:
                    sendTextMessage("Sorry, 'Find Cinemas' is not yet implemented.");
                    //TODO: films_find_cinemas
                    break;
                case no_fullinfo:
                    if (
                        sessionContext == SessionContext.EXPLORING_BARS ||
                            sessionContext == SessionContext.EXPLORING_RESTAURANTS
                        ) {
                        String itemPluralName = "Restaurants";
                        if (sessionContext == SessionContext.EXPLORING_BARS) {
                            itemPluralName = "Bars & Pubs";
                        }

                        blockService.sendVenuesRemainingBlock(
                            this,
                            itemPluralName
                        );
                    }
                    break;
                case venues_book:
                    onIntentVenuesBook();
                    break;

                default:
                    sendTextMessage("Sorry, an error occurred.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendTextMessage("Sorry, an error occurred.");
        }
    }
}
