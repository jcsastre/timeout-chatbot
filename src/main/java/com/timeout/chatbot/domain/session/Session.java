package com.timeout.chatbot.domain.session;

import ai.api.AIServiceException;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import com.timeout.chatbot.blocks.MainOptionsSendBlock;
import com.timeout.chatbot.blocks.RestaurantSummarySendBlock;
import com.timeout.chatbot.blocks.RestaurantsPageSendBlock;
import com.timeout.chatbot.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.domain.Page;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.response.search.page.Response;
import com.timeout.chatbot.graffitti.endpoints.GraffittiEndpoints;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

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

    private final WelcomeMessageSendBlock welcomeMessageSendBlock;
    private final MainOptionsSendBlock mainOptionsSendBlock;
    private final RestaurantSummarySendBlock restaurantSummarySendBlock;
    private final RestaurantsPageSendBlock restaurantsPageSendBlock;

    private final UserRepository userRepository;

    private long lastAccessTime;

    public Session(
        RestTemplate restTemplate,
        GraffittiService graffittiService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        Page page,
        User user,
        WelcomeMessageSendBlock welcomeMessageSendBlock,
        MainOptionsSendBlock mainOptionsSendBlock,
        RestaurantSummarySendBlock restaurantSummarySendBlock,
        RestaurantsPageSendBlock restaurantsPageSendBlock,
        UserRepository userRepository
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.welcomeMessageSendBlock = welcomeMessageSendBlock;
        this.mainOptionsSendBlock = mainOptionsSendBlock;
        this.restaurantSummarySendBlock = restaurantSummarySendBlock;
        this.restaurantsPageSendBlock = restaurantsPageSendBlock;
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
        final ApiaiIntent apiaiIntent = ApiaiIntent.fromString(apiaiAction);

        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
        if (apiaiParameters != null) {
            updateFilterParams(apiaiParameters);
        }

        printConsole(apiaiAction, apiaiParameters);

        switch (apiaiIntent) {
            case GREETINGS:
                onIntentGreetings();
                break;
            case FIND_RESTAURANTS:
                onIntentRestaurants();
                break;
            case FIND_OFFERS:
//                fsm.apply(Intent.FIND_OFFERS);
                break;
            case SET_LOCATION:
//                fsm.apply(Intent.FIND_CAMPINGS);
                break;
            case UNKOWN:
//                onIntentUnknown();
                break;
        }
    }

    public void applyLocation(Double latitude, Double longitude) {
        sessionContextBag.setLocation(
            sessionContextBag.new Location(
                latitude,
                longitude
            )
        );

        //TODO: check context to see if further actions are required
        if(sessionContextState == SessionContextState.EXPLORING_RESTAURANTS) {
            onIntentRestaurants();
        } else {
            //TODO:
        }
    }

    public void applyPayloadAsJsonString(String payload) {
        final JSONObject payloadAsJson = new JSONObject(payload);

        try {
            PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));
            switch (payloadType) {
                case get_started:
                    userRepository.deleteAll(); //TODO: delte!!!
                    userRepository.save(user);
                    welcomeMessageSendBlock.send(user);
                    mainOptionsSendBlock.send(user);
                    break;
                case utterance:
                    final String utterance = payloadAsJson.getString("utterance");
                    applyUtterance(utterance);
                    break;
                case restaurant_get_a_summary:
                    restaurantSummarySendBlock.send(
                        user.getMessengerId(),
                        payloadAsJson.getString("restaurant_id")
                    );
                    break;
                case set_location:
                    //TODO
                    break;
                case restaurants_set_cuisine:
                    //TODO:
                    break;
                default:
                    sendTextMessage("Lo siento ha ocurrido un error.");
                    break;
            }
        } catch(Exception e) {
            sendTextMessage("Lo siento ha ocurrido un error.");
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

    private void onIntentGreetings() {
        if (sessionContextState == SessionContextState.UNDEFINED) {
            sessionContextState = SessionContextState.GREETINGS;
            mainOptionsSendBlock.send(user);
        } else {
            sendTextMessage("¡Hola!");
        }
    }

    private void onIntentRestaurants() {
        this.sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;

        String lookingTxt = "Looking for restaurants in London";

        String url = GraffittiEndpoints.RESTAURANTS.toString();

        final SessionContextBag.Location location = sessionContextBag.getLocation();
        if (location!=null) {
            final Double latitude = sessionContextBag.getLocation().getLatitude();
            final Double longitude = sessionContextBag.getLocation().getLongitude();
            url = url + "&latitude="+latitude+"&longitude="+longitude+"&radius=0.5";
            lookingTxt = lookingTxt + " within 500 meters from you";
        }

        sendTextMessage(lookingTxt);

        final Response response =
            restTemplate.getForObject(
                url,
                Response.class
            );

        int totalItems = response.getMeta().getTotalItems();
        if (totalItems>0) {
            restaurantsPageSendBlock.send(
                user,
                response.getPageItems(),
                response.getMeta().getTotalItems()
            );
        } else {
            sendTextMessage("There are not available restaurants for your request.");
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
}
