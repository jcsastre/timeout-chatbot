package com.timeout.chatbot.session;

import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.timeout.chatbot.block.booking.BookingBlocksHelper;
import com.timeout.chatbot.domain.Page;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.repository.UserRepository;
import com.timeout.chatbot.services.ApiAiService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.context.BookingSessionContext;
import com.timeout.chatbot.session.context.SessionContext;
import com.timeout.chatbot.session.context.LookingSessionContext;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class Session {
    private final UUID uuid;
    private final Page page;
    private final User user;

    private final SessionContext sessionContext;
    private final LookingSessionContext lookingSessionContext;
    private final BookingSessionContext bookingSessionContext;

    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final ApiAiService apiAiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

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

        this.lookingSessionContext = new LookingSessionContext();

        this.bookingSessionContext =
            new BookingSessionContext(this, this.user, messengerSendClientWrapper, this.bookingBlocksHelper);

        this.sessionContext = this.lookingSessionContext;
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
        sessionContext.applyUtterance(utterance);
    }

    public void handleAttachmentMessageEvent(AttachmentMessageEvent event) {
        final List<AttachmentMessageEvent.Attachment> attachments = event.getAttachments();
        for (AttachmentMessageEvent.Attachment attachment : attachments) {
            if (attachment.getType() == AttachmentMessageEvent.AttachmentType.LOCATION) {
                final AttachmentMessageEvent.LocationPayload locationPayload =
                    attachment.getPayload().asLocationPayload();

                applyLocation(
                    locationPayload.getCoordinates().getLatitude(),
                    locationPayload.getCoordinates().getLongitude()
                );
            }
        }
    }

    public void handleTextMessageEvent(
        TextMessageEvent event
    ) {
        applyUtterance(event.getText());
    }

    public void handleQuickReplyMessageEvent(
        QuickReplyMessageEvent event
    ) {
        applyPayload(event.getQuickReply().getPayload());
    }

    public void handlePostbackEvent(
        PostbackEvent event
    ) {
        applyPayload(event.getPayload());
    }

    public void applyPayload(
        String payloadAsJsonString
    ) {
        sessionContext.applyPayload(payloadAsJsonString);
    }


    public void applyLocation(Double latitude, Double longitude) {
        sessionContextBag.setGeolocation(
            sessionContextBag.new Geolocation(
                latitude,
                longitude
            )
        );

        //TODO: check context to see if further actions are required
        if(sessionContext == SessionContext.EXPLORING_RESTAURANTS) {
            onIntentFindRestaurants();
        } else if (sessionContext == SessionContext.EXPLORING_BARS) {
            onIntentFindBars();
        } else if (sessionContext == SessionContext.EXPLORING_FILMS) {
            onIntentFindFilms(1);
        } else {
            //TODO:
        }
    }

    private void onIntentVenuesBook()
    {
        if (sessionContext != SessionContext.BOOKING)
        {
            sessionContext = SessionContext.BOOKING;
            bookingSessionContext.startBooking();
        }
    }

    private void onIntentDiscover()
    {
        blockService.sendDiscoverBlock(
            user
        );
    }

    private void onIntentGreetings()
    {
        if (sessionContext == SessionContext.UNDEFINED) {
            blockService.sendWelcomeBackBlock(user);

            sessionContext = SessionContext.SUGGESTIONS;

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
        sessionContext = SessionContext.SUGGESTIONS;

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
        this.sessionContext = SessionContext.EXPLORING_FILMS;

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
        this.sessionContext = SessionContext.EXPLORING_BARS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            onIntentFindBars();
        }
    }

    private void onIntentFindBars() {
        if (sessionContext != SessionContext.EXPLORING_BARS) {
            sessionContext = SessionContext.EXPLORING_BARS;
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
        this.sessionContext = SessionContext.EXPLORING_RESTAURANTS;

        if (sessionContextBag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(user.getMessengerId());
        } else {
            onIntentFindRestaurants();
        }
    }

    private void onIntentFindRestaurants() {
        if (sessionContext != SessionContext.EXPLORING_RESTAURANTS) {
            sessionContext = SessionContext.EXPLORING_RESTAURANTS;
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

    @Override
    public String toString() {
        return String.format(
            "Session[uuid=%s, page=%s, user=%s]",
            uuid, page, user
        );
    }
}
