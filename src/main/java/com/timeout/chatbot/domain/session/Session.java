package com.timeout.chatbot.domain.session;

import ai.api.AIServiceException;
import ai.api.model.Result;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.apiai.ApiaiIntent;
import com.timeout.chatbot.domain.messenger.Page;
import com.timeout.chatbot.domain.messenger.Recipient;
import com.timeout.chatbot.graffiti.endpoints.GraffittiEndpoints;
import com.timeout.chatbot.graffitti.domain.response.Response;
import com.timeout.chatbot.platforms.messenger.send.blocks.RestaurantsPage;
import com.timeout.chatbot.platforms.messenger.send.blocks.WelcomeMessageSendBlock;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

public class Session {
    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final ApiAiService apiAiService;
    private final MessengerSendClient messengerSendClient;

    private final UUID uuid;

    private final Page page;
    private final Recipient recipient;

    private SessionContextState sessionContextState;
    private final SessionContextBag sessionContextBag;

    private final WelcomeMessageSendBlock welcomeMessageSendBlock;

    public Session(
        RestTemplate restTemplate,
        GraffittiService graffittiService,
        ApiAiService apiAiService,
        MessengerSendClient messengerSendClient,
        Page page,
        Recipient recipient,
        WelcomeMessageSendBlock welcomeMessageSendBlock
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.apiAiService = apiAiService;
        this.messengerSendClient = messengerSendClient;
        this.welcomeMessageSendBlock = welcomeMessageSendBlock;

        this.uuid = UUID.randomUUID();

        this.page = page;
        this.recipient = recipient;

        this.sessionContextState = SessionContextState.UNDEFINED;
        this.sessionContextBag = new SessionContextBag();

//        OnEnterGreetingsHandler onEnterGreetingsHandler =
//            new OnEnterGreetingsHandler(
//                this,
//                this.messengerSendClient
//            );
//
//        OnEnterExploringCampingsHandler onEnterExploringCampingsHandler =
//            new OnEnterExploringCampingsHandler(
//                this.recipient,
//                this.filterParams,
//                this.graffittiService,
//                this.messengerSendClient
//            );
//
//        OnEnterExploringOffersHandler onEnterExploringOffersHandler =
//            new OnEnterExploringOffersHandler(
//                this.recipient,
//                this.filterParams,
//                this.graffittiService,
//                this.messengerSendClient
//            );
    }

    public UUID getUuid() {
        return uuid;
    }

    public Recipient getRecipient() {
        return recipient;
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

    private void onIntentGreetings() {
        if (sessionContextState == SessionContextState.UNDEFINED) {
            sessionContextState = SessionContextState.GREETINGS;
            welcomeMessageSendBlock.send(recipient);
        } else {
            sendTextMessage("¡Hola!");
        }
    }

    private void onIntentRestaurants() {
        this.sessionContextState = SessionContextState.EXPLORING_RESTAURANTS;

        // Fetch api
        final Response response = restTemplate.getForObject(GraffittiEndpoints.RESTAURANTS.toString(), Response.class);

        final Integer totalItems = response.getMeta().getTotalItems();

        new RestaurantsPage(
            messengerSendClient,
            response.getItems(),
            recipient.getUid()
        ).send();
        //TODO: send option to paginate
    }

    private void onIntentUnknown() {
        sendTextMessage("Lo siento, no te entiendo");
    }

    public void sendTextMessage(String msg) {
        try {
            this.messengerSendClient.sendTextMessage(
                this.recipient.getUid(),
                msg
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
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
            try {
                messengerSendClient.sendTextMessage(recipient.getUid(), "Lo siento ha habido un problema");
            } catch (MessengerApiException | MessengerIOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        if (apiaiResult == null) {
            try {
                messengerSendClient.sendTextMessage(recipient.getUid(), "Lo siento ha habido un problema");
            } catch (MessengerApiException | MessengerIOException e1) {
                e1.printStackTrace();
            }
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
}
