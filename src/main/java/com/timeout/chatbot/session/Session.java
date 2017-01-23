package com.timeout.chatbot.session;

import ai.api.model.Result;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.timeout.chatbot.block.booking.BookingBlocksHelper;
import com.timeout.chatbot.domain.page.Page;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.context.SessionContext;
import com.timeout.chatbot.session.context.SessionContextBooking;
import com.timeout.chatbot.session.context.SessionContextLooking;
import com.timeout.chatbot.session.context.SessionState;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class Session {
    private final UUID uuid;
    private final Page page;
    private final User user;

    private SessionState sessionState;
    private final SessionContextLooking sessionContextLooking;
    private final SessionContextBooking sessionContextBooking;

    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final ApiAiService apiAiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    private final SessionStateLookingBag sessionStateLookingBag;
    private final SessionContextBag sessionContextBag;

    private final BlockService blockService;

    private final UserRepository userRepository;

    private long lastAccessTime;

    private final static int NUMBER_ITEMS_THRESOLD = 100;

    private final BookingBlocksHelper bookingBlocksHelper;

    public Session(
        RestTemplate restTemplate,
        GraffittiService graffittiService,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        Page page,
        User user,
        BlockService blockService,
        UserRepository userRepository,
        BookingBlocksHelper bookingBlocksHelper
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.apiAiService = apiAiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.blockService = blockService;
        this.userRepository = userRepository;
        this.bookingBlocksHelper = bookingBlocksHelper;

        this.uuid = UUID.randomUUID();

        this.page = page;
        this.user = user;

        this.sessionContextBag = new SessionContextBag();

        this.sessionContextLooking = new SessionContextLooking(
            this.user,
            this.apiAiService,
            messengerSendClientWrapper
        );

        this.sessionContextBooking = new SessionContextBooking(
            this.user,
            this.apiAiService,
            this.messengerSendClientWrapper,
            this.bookingBlocksHelper
        );

        this.sessionStateLookingBag = new SessionStateLookingBag();

        this.sessionState = SessionState.UNDEFINED;
    }

    public void handleQuickReplyMessageEvent(
        QuickReplyMessageEvent event
    ) {
        sessionContext.handleQuickReplyMessageEvent(event);
    }

    public void handlePostbackEvent(
        PostbackEvent event
    ) {
        sessionContext.handlePostbackEvent(event);
    }

    public void handleAttachmentMessageEvent(
        AttachmentMessageEvent event
    ) {
        sessionContext.handleAttachmentMessageEvent(event);
    }

    private void handleTextOnStateLooking(
        String text
    ) {
        final Result apiaiResult = getApiaiResult(text);
        if (apiaiResult != null) {
            handleApiResult(apiaiResult);
        } else {
            sendTextMessage("Sorry, I don't understand you");
        }
    }

    private void handleText(
        String text
    ) {

        final Result apiaiResult = getApiaiResult(text);
        if (apiaiResult != null) {
            handleApiResult(apiaiResult);
        } else {
            blockService.sendWelcomeBackBlock(user);
            blockService.sendSuggestionsBlock(user);
            blockService.sendDiscoverBlock(user);
        }
    }


//    private void handleApiResult(
//        Result apiaiResult
//    ) {
//        final String apiaiAction = apiaiResult.getAction();
//        final ApiaiIntent apiaiIntent = ApiaiIntent.fromApiaiAction(apiaiAction);
//
//        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
////        if (apiaiParameters != null) {
////            updateFilterParams(apiaiParameters);
////        }
//
//        SessionUtils.printConsole(apiaiAction, apiaiParameters);
//
//        switch (apiaiIntent) {
//            case GREETINGS:
//                switch (sessionState) {
//                    case LOOKING:
//                        sendTextMessage("Hi!");
//                        break;
//                }
//                onIntentGreetings();
//                break;
//            case SUGGESTIONS:
//                onIntentSuggestions();
//                break;
//            case FIND_THINGSTODO:
//                onIntentFindThingstodo();
//                break;
//            case FIND_RESTAURANTS:
//                onIntentFindRestaurants();
//                break;
//            case FIND_RESTAURANTS_NEARBY:
//                onIntentFindRestaurantsNearby();
//                break;
//            case FIND_BARSANDPUBS:
//                onIntentFindBars();
//                break;
//            case FIND_BARSANDPUBS_NEARBY:
//                onIntentFindBarsNearby();
//                break;
//            case FIND_ART:
//                onIntentFindArt();
//                break;
//            case FIND_THEATRE:
//                onIntentFindTheatre();
//                break;
//            case FIND_MUSIC:
//                onIntentFindMusic();
//                break;
//            case FIND_NIGHTLIFE:
//                onIntentFindNightlife();
//                break;
//            case FINDS_FILMS:
//                onIntentFindFilms(1);
//                break;
//            case SET_LOCATION:
//                //                fsm.apply(Intent.FIND_CAMPINGS);
//                break;
//            case UNKOWN:
//                //                onIntentUnknown();
//                break;
//            case DISCOVER:
//                onIntentDiscover();
//                break;
//            default:
//                messengerSendClientWrapper.sendTextMessage(
//                    user.getMessengerId(),
//                    "Sorry, I don't understand you."
//                );
//                break;
//        }
//    }

//    private void updateFilterParams(Map<String, JsonElement> apiaiParameters) {
//        final FilterParams filterParams = getFilterParams();
//        for (Map.Entry<String, JsonElement> parameter : apiaiParameters.entrySet()) {
//            if (parameter.getKey().equals("location")) {
//                String location = parameter.getValue().getAsString();
//                filterParams.setPlace(
//                    new Place(location)
//                );
//            }
//        }
//    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public SessionContextBag getSessionContextBag() {
        return sessionContextBag;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
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

    public User getUser() {
        return user;
    }

    public Page getPage() {
        return page;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public SessionStateLookingBag getSessionStateLookingBag() {
        return sessionStateLookingBag;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[uuid=%s, page=%s, user=%s]",
            uuid, page, user
        );
    }
}
