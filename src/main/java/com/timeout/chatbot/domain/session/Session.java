package com.timeout.chatbot.domain.session;

import ai.api.AIServiceException;
import ai.api.model.Result;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.Page;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.endpoints.BarsSearchEndpoint;
import com.timeout.chatbot.graffitti.endpoints.RestaurantsSearchEndpoint;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public class Session {
    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final ApiAiService apiAiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    private final UUID uuid;

    private final Page page;
    private final User user;

    private SessionContextState sessionContextState;
    private final SessionContextBag sessionContextBag;

    private final BlockService blockService;

    private final UserRepository userRepository;

    private long lastAccessTime;

    private final static int NUMBER_ITEMS_THRESOLD = 100;

    public Session(
        RestTemplate restTemplate,
        GraffittiService graffittiService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        Page page,
        User user,
        BlockService blockService,
        UserRepository userRepository
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.blockService = blockService;
        this.userRepository = userRepository;

        this.uuid = UUID.randomUUID();

        this.page = page;
        this.user = user;

        this.sessionContextState = SessionContextState.UNDEFINED;
        this.sessionContextBag = new SessionContextBag();
    }

    public UUID getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public Page getPage() {
        return page;
    }

    public void applyUtterance(
        String utterance
    ) {
        Result apiaiResult = getApiaiResult(utterance);

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
                onIntentFindRestaurants(1);
                break;
            case FIND_RESTAURANTS_NEARBY:
                onIntentFindRestaurantsNearby();
                break;
            case FIND_BARSANDPUBS:
                onIntentFindBars(1);
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
        }
    }

    public void applyLocation(Double latitude, Double longitude) {
        sessionContextBag.setGeolocation(
            sessionContextBag.new Geolocation(
                latitude,
                longitude
            )
        );

        //TODO: check context to see if further actions are required
        if(sessionContextState == SessionContextState.EXPLORING_RESTAURANTS) {
            onIntentFindRestaurants(1);
        } else if (sessionContextState == SessionContextState.EXPLORING_BARS) {
            onIntentFindBars(1);
        } else if (sessionContextState == SessionContextState.EXPLORING_FILMS) {
            onIntentFindFilms(1);
        } else {
            //TODO:
        }
    }

    public void applyPayload(String payloadAsJsonString) {
        final JSONObject payloadAsJson = new JSONObject(payloadAsJsonString);

        try {
            PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));
            switch (payloadType) {
                case get_started:
                    // userRepository.deleteAll(); //TODO: delte!!!
                    userRepository.save(user);
                    blockService.sendWelcomeFirstTimeBlock(user);
                    blockService.sendMainOptionsBlock(user);
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
                    final int nextPageNumber = payloadAsJson.getInt("next_page_number");
                    if (sessionContextState==SessionContextState.EXPLORING_BARS) {
                        onIntentFindBars(
                            nextPageNumber
                        );
                    } else if (sessionContextState==SessionContextState.EXPLORING_RESTAURANTS) {
                        onIntentFindRestaurants(
                            nextPageNumber
                        );
                    }
                    break;
                case venues_book:
                    sendTextMessage("Sorry, restaurants booking is not yet implemented.");
                    //TODO: venues_book
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
                default:
                    sendTextMessage("Sorry, an error occurred.");
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            sendTextMessage("Sorry, an error occurred.");
        }

//        try {
//            final String type = new JSONObject(event.getPayload()).getString("type");
//
//            if (type.equals("restaurants")) {
//                session.applyUtterance("restaurants");
//            }
//        } catch(JSONException exception) {
//            //TODO
//        }
    }

    private void onIntentDiscover() {
        blockService.sendDiscoverBlock(
            user
        );
    }

    private void onIntentGreetings() {
        if (sessionContextState == SessionContextState.UNDEFINED) {
            blockService.sendWelcomeBackBlock(user);

            sessionContextState = SessionContextState.SUGGESTIONS;

            blockService.sendSuggestionsBlock(user);

            messengerSendClientWrapper.sendTemplate(
                user.getMessengerId(),
                    ButtonTemplate.newBuilder(
                        "If my suggestions doesn't meet your needs, just type 'discover'",
                        Button.newListBuilder()
                            .addPostbackButton(
                                "Discover",
                                new JSONObject()
                                    .put("type", "utterance")
                                    .put("utterance", "Discover")
                                    .toString()
                            ).toList()
                            .build()
                    ).build()
            );

//            final TilesResponse tilesResponse =
//                restTemplate.getForObject(
//                    GraffittiEndpoints.HOME.toString(),
//                    TilesResponse.class
//                );

//            blockService.sendDiscoverBlock(
//                user
//            );
//
//            if (!user.getSuggestionsDone().getDiscover()) {
//                sendTextMessage("If you want to see again this, just type 'discover'");
//                user.getSuggestionsDone().setDiscover(true);
//                userRepository.save(user);
//            }
        } else {
            sendTextMessage("¡Hola!");
        }
    }

    private void onIntentSuggestions() {
        sessionContextState = SessionContextState.SUGGESTIONS;

        blockService.sendSuggestionsBlock(user);
    }

    private void onIntentFindThingstodo() {
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "Sorry, 'Things to do' is not yet implemented."
        );
    }

    private void onIntentFindArt() {
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "Sorry, 'Art' is not yet implemented."
        );
    }

    private void  onIntentFindTheatre() {
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "Sorry, 'Theatre' is not yet implemented."
        );
    }

    private void onIntentFindMusic() {
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "Sorry, 'Music' is not yet implemented."
        );
    }

    private void onIntentFindNightlife() {
        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            "Sorry, 'Nightlife' is not yet implemented."
        );
    }

    private void onIntentFindFilms(@NotNull Integer pageNumber) {
        this.sessionContextState = SessionContextState.EXPLORING_FILMS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            blockService.sendFilmsPageBlock(
                this,
                pageNumber
            );
        }
    }

    private void onIntentFindBarsNearby() {
        this.sessionContextState = SessionContextState.EXPLORING_BARS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            onIntentFindBars(1);
        }
    }

    private void onIntentFindBars(@NotNull Integer pageNumber) {
        sessionContextState = SessionContextState.EXPLORING_BARS;

        String url = null;
        String lookingTxt = null;

        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();
        if (geolocation == null) {
            url = BarsSearchEndpoint.getUrl(
                "en-GB",
                9,
                pageNumber
            );

            lookingTxt = "Looking for Bars & Pubs in London";
        } else {
            url = BarsSearchEndpoint.getUrl(
                "en-GB",
                9,
                pageNumber,
                sessionContextBag.getGeolocation().getLatitude(),
                sessionContextBag.getGeolocation().getLongitude()
            );

            lookingTxt = "Looking for Bars & Pubs within 500 meters from the current location.";
        }

        sendTextMessage(lookingTxt);

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                url,
                SearchResponse.class
            );

        if (searchResponse.getMeta().getTotalItems() > 0) {
            blockService.sendVenuesPageBlock(
                user.getMessengerId(),
                sessionContextBag.getGeolocation(),
                searchResponse.getPageItems(),
                "Bars & Pubs",
                searchResponse.getNextPageNumber(),
                searchResponse.getReaminingItems()
            );

//            if (suggestionRestaurantsFineSearchRequired) {
//                user.getSuggestionsDone().setRestaurantsFineSearch(true);
//                userRepository.save(user);
//            }
        } else {
            sendTextMessage("There are not available Bars or Pubs for your request.");
        }
    }

    private void onIntentFindRestaurantsNearby() {
        this.sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            onIntentFindRestaurants(1);
        }

    }

    private void onIntentFindRestaurants(@NotNull Integer pageNumber) {
        this.sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;

        String url = null;
        String lookingTxt = null;

        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();
        if (geolocation == null) {
            url = RestaurantsSearchEndpoint.getUrl(
                "en-GB",
                10,
                pageNumber
            );

            lookingTxt =
                "Looking for Restaurants in London. Please, give me a moment.";
        } else {
            url = RestaurantsSearchEndpoint.getUrl(
                "en-GB",
                10,
                pageNumber,
                sessionContextBag.getGeolocation().getLatitude(),
                sessionContextBag.getGeolocation().getLongitude()
            );

            lookingTxt =
                "Looking for Restaurants within 500 meters from the current location. Please, give me a moment.";
        }

        sendTextMessage(lookingTxt);

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                url,
                SearchResponse.class
            );

        if (searchResponse.getMeta().getTotalItems() > 0) {
            blockService.sendVenuesPageBlock(
                user.getMessengerId(),
                sessionContextBag.getGeolocation(),
                searchResponse.getPageItems(),
                "Restaurants",
                searchResponse.getNextPageNumber(),
                searchResponse.getReaminingItems()
            );
        } else {
            sendTextMessage("There are not available Restaurants for your request.");
        }
    }

    private void onIntentUnknown() {
        sendTextMessage("Lo siento, no te entiendo");
    }

    public void sendTextMessage(String msg) {
        if (msg.length() > 320) {
            msg = msg.substring(0, 320);
        }

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            msg
        );
    }

    private void updateFilterParams(Map<String, JsonElement> apiaiParameters) {
//        final FilterParams filterParams = getFilterParams();
//        for (Map.Entry<String, JsonElement> parameter : apiaiParameters.entrySet()) {
//            if (parameter.getKey().equals("location")) {
//                String location = parameter.getValue().getAsString();
//                filterParams.setPlace(
//                    new Place(location)
//                );
//            }
//        }
    }

    private Result getApiaiResult(String utterance) {
        Result apiaiResult = null;
        try {
            apiaiResult = apiAiService.processText(utterance);
        } catch (AIServiceException e) {
            messengerSendClientWrapper.sendTextMessage(user.getMessengerId(), "Lo siento ha habido un problema");
            e.printStackTrace();
        }

        if (apiaiResult == null) {
            messengerSendClientWrapper.sendTextMessage(user.getMessengerId(), "Lo siento ha habido un problema");
        }

        return apiaiResult;
    }

    private void printConsole(String action, Map<String, JsonElement> apiaiParameters) {
        System.out.println("▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");

        if (action != null) {
            System.out.println("ApiaiIntent: " + action);
        }

        if (apiaiParameters != null) {
            for (Map.Entry<String, JsonElement> parameter : apiaiParameters.entrySet()) {
                System.out.println("Parameter <" + parameter.getKey() + ">: " + parameter.getValue().getAsString());
            }
        }
        System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
        System.out.println();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public SessionContextBag getSessionContextBag() {
        return sessionContextBag;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[uuid=%s, page=%s, user=%s]",
            uuid, page, user
        );
    }
}
