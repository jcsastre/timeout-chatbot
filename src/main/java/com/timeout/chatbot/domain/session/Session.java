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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private BookingState bookingState;
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
            onIntentFindRestaurants();
        } else if (sessionContextState == SessionContextState.EXPLORING_BARS) {
            onIntentFindBars();
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
                    if (sessionContextState==SessionContextState.EXPLORING_BARS) {
                        onIntentFindBars();
                    } else if (sessionContextState==SessionContextState.EXPLORING_RESTAURANTS) {
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
                        sessionContextState==SessionContextState.EXPLORING_BARS ||
                        sessionContextState==SessionContextState.EXPLORING_RESTAURANTS
                    ) {
                        String itemPluralName = "Restaurants";
                        if (sessionContextState==SessionContextState.EXPLORING_BARS) {
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
                case booking_people_count:
                    final String count = payloadAsJson.getString("count");
                    sessionContextBag.getBookingBag().setPeopleCount(new Integer(count));
                    onIntentVenuesBook();
                    break;
                case booking_date:
                    final String date = payloadAsJson.getString("date");
                    if (date.equalsIgnoreCase("today")) {
                        sessionContextBag.getBookingBag().setLocalDate(LocalDate.now());
                    } else if (date.equalsIgnoreCase("tomorrow")) {
                        sessionContextBag.getBookingBag().setLocalDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
                    } else {
                        //TODO
                    }
                    onIntentVenuesBook();
                    break;
                case booking_time:
                    final String time = payloadAsJson.getString("time");
                    sessionContextBag.getBookingBag().setLocalTime(LocalTime.of(new Integer(time), 0));
                    onIntentVenuesBook();
                    break;
                case booking_first_name_fb_ok:
                    sendTextMessage("Great");
                    sessionContextBag.getBookingBag().setFirstName(getUser().getFbUserProfile().getFirstName());
                    onIntentVenuesBook();
                    break;
                case booking_first_name_fb_not_ok:
                    //TODO: ask user to type his first name
                    break;
                case booking_last_name_fb_ok:
                    sendTextMessage("Great");
                    sessionContextBag.getBookingBag().setLastName(getUser().getFbUserProfile().getLastName());
                    onIntentVenuesBook();
                    break;
                case booking_last_name_fb_not_ok:
                    //TODO: ask user to type his first name
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

    private void onIntentVenuesBook() {

        if (sessionContextState != SessionContextState.BOOKING) {
            sessionContextState = SessionContextState.BOOKING;
            bookingState = BookingState.PEOPLE_COUNT;

            sendTextMessage (
                "IMPORTANT: Because I'm a chatbot under development, the booking feature is just a simulation. " +
                    "I'm not making a real booking :)"
            );
        }

        switch (bookingState) {
            case PEOPLE_COUNT:
                blockService.sendBookingPeopleCountBlock(user);
                break;
            default:
                sendTextMessage("Sorry, an error occurred.");
                break;
        }



        final SessionContextBag.BookingBag bookingBag = getSessionContextBag().getBookingBag();
        final Integer peopleCount = bookingBag.getPeopleCount();
        if (peopleCount == null) {

            return;
        }

        final LocalDate localDate = bookingBag.getLocalDate();
        if (localDate == null) {
            blockService.sendBookingDateBlock(user);
            return;
        }

        final LocalTime localTime = bookingBag.getLocalTime();
        if (localTime == null) {
            blockService.sendBookingTimeBlock(user);
            return;
        }

        String firstName = bookingBag.getFirstName();
        if (firstName == null) {
            firstName = user.getFbUserProfile().getFirstName();
            if (firstName != null) {
                blockService.sendBookingFirstnameConfirmationBlock(user);
                return;
            } else {
                //TODO: ask for first name
            }
        }

        String las = bookingBag.getLastName();
        if (las == null) {
            las = user.getFbUserProfile().getLastName();
            if (las != null) {
                blockService.sendBookingLastnameConfirmationBlock(user);
                return;
            } else {
                //TODO: ask for last name
            }
        }

        final String email = bookingBag.getEmail();
        if (email != null) {
            sendTextMessage("What is your email?");
            return;
        }
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

            sendMySuggestionsDoesnot();

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

    private void sendMySuggestionsDoesnot() {
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
            onIntentFindBars();
        }
    }

    private void onIntentFindBars() {
        if (sessionContextState != SessionContextState.EXPLORING_BARS) {
            sessionContextState = SessionContextState.EXPLORING_BARS;
            sessionContextBag.setPageNumber(1);
        } {
            Integer pageNumber = sessionContextBag.getPageNumber();
            if (pageNumber != null) {
                pageNumber++;
                sessionContextBag.setPageNumber(pageNumber);;
            } else {
                sessionContextBag.setPageNumber(1);
            }
        }

        String url = null;
        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();
        if (geolocation == null) {
            url = BarsSearchEndpoint.getUrl(
                "en-GB",
                10,
                sessionContextBag.getPageNumber()
            );

            sendTextMessage(
                "Looking for Bars & Pubs in London"
            );
        } else {
            url = BarsSearchEndpoint.getUrl(
                "en-GB",
                10,
                sessionContextBag.getPageNumber(),
                geolocation.getLatitude(),
                geolocation.getLongitude()
            );

            sendTextMessage(
                "Looking for Bars & Pubs within 500 meters from the current location."
            );
        }

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                url,
                SearchResponse.class
            );

        if (searchResponse.getMeta().getTotalItems() > 0) {
            sessionContextBag.setReaminingItems(searchResponse.getRemainingItems());

            blockService.sendVenuesPageBlock(
                this,
                searchResponse.getPageItems(),
                "Bars & Pubs"
            );

            blockService.sendVenuesRemainingBlock(
                this,
                "Bars & Pubs"
            );
        } else {
            sendTextMessage("There are not available Bars or Pubs for your request.");
        }
    }

    private void onIntentFindRestaurantsNearby() {
        this.sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            onIntentFindRestaurants();
        }
    }

    private void onIntentFindRestaurants() {
        if (sessionContextState != SessionContextState.EXPLORING_RESTAURANTS) {
            sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;
            sessionContextBag.setPageNumber(1);
        } {
            Integer pageNumber = sessionContextBag.getPageNumber();
            if (pageNumber != null) {
                pageNumber++;
                sessionContextBag.setPageNumber(pageNumber);;
            } else {
                sessionContextBag.setPageNumber(1);
            }
        }

        String url = null;
        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();
        if (geolocation == null) {
            url = RestaurantsSearchEndpoint.getUrl(
                "en-GB",
                10,
                sessionContextBag.getPageNumber()
            );

            sendTextMessage(
                "Looking for Restaurants in London. Please, give me a moment."
            );
        } else {
            url = RestaurantsSearchEndpoint.getUrl(
                "en-GB",
                10,
                sessionContextBag.getPageNumber(),
                geolocation.getLatitude(),
                geolocation.getLongitude()
            );

            sendTextMessage(
                "Looking for Restaurants within 500 meters from the current location. Please, give me a moment."
            );
        }

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                url,
                SearchResponse.class
            );

        if (searchResponse.getMeta().getTotalItems() > 0) {
            sessionContextBag.setReaminingItems(searchResponse.getRemainingItems());

            blockService.sendVenuesPageBlock(
                this,
                searchResponse.getPageItems(),
                "Restaurants"
            );

            blockService.sendVenuesRemainingBlock(
                this,
                "Restaurants"
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
