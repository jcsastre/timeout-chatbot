package com.timeout.chatbot.session.context;

import ai.api.model.Result;
import com.github.messenger4j.receive.events.AttachmentMessageEvent;
import com.github.messenger4j.receive.events.PostbackEvent;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetChild;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.ApiAiService;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SessionContextLooking extends SessionContext {

    private final String graffittiWhat;
    private final String graffittiWhen;
    private final GraffittiFacetV4FacetChild graffittiWhere;
    private final GraffittiType graffittiType;
    private final Integer graffittiPage;

    public SessionContextLooking(
        User user,
        ApiAiService apiAiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        super(user, apiAiService, messengerSendClientWrapper);

        this.graffittiWhat = null;
        this.graffittiWhen = null;
        this.graffittiWhere = null;
        this.graffittiType = null;
        this.graffittiPage = null;
    }

    @Override
    public void handleTextMessageEvent(
        TextMessageEvent event
    ) {
        final String text = event.getText();

        final Result apiaiResult = getApiaiResult(text);
        if (apiaiResult != null) {
            handleApiResult(apiaiResult);
        } else {
            messengerSendClientWrapper.sendTextMessage(
                user.getMessengerId(),
                "Sorry, I don't understand you"
            );
        }
    }

    @Override
    public void handlePostbackEvent(PostbackEvent event) {

    }

    @Override
    public void handleQuickReplyMessageEvent(QuickReplyMessageEvent event) {

    }

    @Override
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

    private void handleApiResult(
        Result apiaiResult
    ) {
//        final String apiaiAction = apiaiResult.getAction();
//        final NluIntentType apiaiIntent = NluIntentType.fromApiaiAction(apiaiAction);
//
//        final Map<String, JsonElement> apiaiParameters = apiaiResult.getParameters();
////        if (apiaiParameters != null) {
////            updateFilterParams(apiaiParameters);
////        }
//
//        printConsole(apiaiAction, apiaiParameters);
//
//        switch (apiaiIntent) {
//            case GREETINGS:
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
//                //                fsm.apply(NluIntent.FIND_CAMPINGS);
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
    }

    public void applyPayload(String payloadAsJsonString) {
//
//        final JSONObject payloadAsJson = new JSONObject(payloadAsJsonString);
//
//        try {
//            PayloadType payloadType = PayloadType.valueOf(payloadAsJson.getString("type"));
//            switch (payloadType) {
//                case get_started:
//                    // userRepository.deleteAll(); //TODO: delte!!!
//                    userRepository.save(user);
//                    blockService.sendWelcomeFirstTimeBlock(user);
//                    blockService.sendSuggestionsBlock(user);
//                    sendMySuggestionsDoesnot();
//                    break;
//                case utterance:
//                    final String utterance = payloadAsJson.getString("utterance");
//                    applyUtterance(utterance);
//                    break;
//                case venues_more_info:
//                    blockService.sendVenueSummaryBlock(
//                        user.getMessengerId(),
//                        payloadAsJson.getString("venue_id")
//                    );
//                    break;
//                case venues_see_more:
//                    if (sessionContext == SessionContext.EXPLORING_BARS) {
//                        onIntentFindBars();
//                    } else if (sessionContext == SessionContext.EXPLORING_RESTAURANTS) {
//                        onIntentFindRestaurants();
//                    }
//                    break;
//                case set_location:
//                    blockService.sendGeolocationAskBlock(user.getMessengerId());
//                    break;
//                case restaurants_set_cuisine:
//                    sendTextMessage("Sorry, set cuisine is not yet implemented.");
//                    //TODO: restaurants_set_cuisine
//                    break;
//                case films_more_info:
//                    sendTextMessage("Sorry, 'More info' is not yet implemented.");
//                    //TODO: films_more_info
//                    break;
//                case films_find_cinemas:
//                    sendTextMessage("Sorry, 'Find Cinemas' is not yet implemented.");
//                    //TODO: films_find_cinemas
//                    break;
//                case no_see_at_timeout:
//                    if (
//                        sessionContext == SessionContext.EXPLORING_BARS ||
//                            sessionContext == SessionContext.EXPLORING_RESTAURANTS
//                        ) {
//                        String itemPluralName = "Restaurants";
//                        if (sessionContext == SessionContext.EXPLORING_BARS) {
//                            itemPluralName = "Bars & Pubs";
//                        }
//
//                        blockService.sendVenuesRemainingBlock(
//                            this,
//                            itemPluralName
//                        );
//                    }
//                    break;
//                case venues_book:
//                    onIntentVenuesBook();
//                    break;
//
//                default:
//                    sendTextMessage("Sorry, an error occurred.");
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            sendTextMessage("Sorry, an error occurred.");
//        }
    }

    public void applyLocation(Double latitude, Double longitude) {
//        sessionContextBag.setGeolocation(
//            sessionContextBag.new Geolocation(
//                latitude,
//                longitude
//            )
//        );
//
//        //TODO: check context to see if further actions are required
//        if(sessionContext == SessionContext.EXPLORING_RESTAURANTS) {
//            onIntentFindRestaurants();
//        } else if (sessionContext == SessionContext.EXPLORING_BARS) {
//            onIntentFindBars();
//        } else if (sessionContext == SessionContext.EXPLORING_FILMS) {
//            onIntentFindFilms(1);
//        } else {
//            //TODO:
//        }
    }

    private void onIntentVenuesBook()
    {
//        if (sessionContext != SessionContext.BOOKING)
//        {
//            sessionContext = SessionContext.BOOKING;
//            sessionContextBooking.startBooking();
//        }
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


    private void onIntentFindFilms(@NotNull Integer pageNumber) {
//        this.sessionContext = SessionContext.EXPLORING_FILMS;
//
//        if (sessionContextBag.getGeolocation() == null) {
//            blockService.sendGeolocationAskBlock(user.getMessengerId());
//        } else {
//            blockService.sendFilmsPageBlock(
//                this,
//                pageNumber
//            );
//        }
    }

    private void onIntentFindBars() {
//        if (sessionContext != SessionContext.EXPLORING_BARS) {
//            sessionContext = SessionContext.EXPLORING_BARS;
//            sessionContextBag.setPageNumber(1);
//        } {
//            Integer pageNumber = sessionContextBag.getPageNumber();
//            if (pageNumber != null) {
//                pageNumber++;
//                sessionContextBag.setPageNumber(pageNumber);;
//            } else {
//                sessionContextBag.setPageNumber(1);
//            }
//        }
//
//        String url = null;
//        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();
//        if (geolocation == null) {
//            url = BarsSearchEndpoint.getUrl(
//                "en-GB",
//                10,
//                sessionContextBag.getPageNumber()
//            );
//
//            sendTextMessage(
//                "Looking for Bars & Pubs in London"
//            );
//        } else {
//            url = BarsSearchEndpoint.getUrl(
//                "en-GB",
//                10,
//                sessionContextBag.getPageNumber(),
//                geolocation.getLatitude(),
//                geolocation.getLongitude()
//            );
//
//            sendTextMessage(
//                "Looking for Bars & Pubs within 500 meters from the current location."
//            );
//        }
//
//        final SearchResponse searchResponse =
//            restTemplate.getForObject(
//                url,
//                SearchResponse.class
//            );
//
//        if (searchResponse.getMeta().getTotalItems() > 0) {
//            sessionContextBag.setReaminingItems(searchResponse.getRemainingItems());
//
//            blockService.sendVenuesPageBlock(
//                this,
//                searchResponse.getPageItems(),
//                "Bars & Pubs"
//            );
//
//            blockService.sendVenuesRemainingBlock(
//                this,
//                "Bars & Pubs"
//            );
//        } else {
//            sendTextMessage("There are not available Bars or Pubs for your request.");
//        }
    }

//    private void onIntentUnknown() {
//        sendTextMessage("Lo siento, no te entiendo");
//    }
}
