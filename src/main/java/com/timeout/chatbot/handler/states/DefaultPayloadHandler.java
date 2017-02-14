package com.timeout.chatbot.handler.states;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.handler.states.booking.BookingBeginHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateLookingBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultPayloadHandler {

    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final DefaultTextHandler defaultTextHandler;
    private final GraffittiService graffittiService;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;
    private final BookingBeginHandler bookingBeginHandler;

    @Autowired
    public DefaultPayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        DefaultTextHandler defaultTextHandler,
        GraffittiService graffittiService,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        BookingBeginHandler bookingBeginHandler
    ) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.defaultTextHandler = defaultTextHandler;
        this.graffittiService = graffittiService;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.bookingBeginHandler = bookingBeginHandler;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                blockService.sendWelcomeFirstTimeBlock(session.getUser());
                blockService.sendVersionInfoBlock(session.getUser().getMessengerId());
                intentService.handleDiscover(session);
//                session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
//                blockService.sendSuggestionsBlock(session);
                break;

            case start_over:
                session.reset();
                blockService.sendWelcomeBackBlock(session.getUser());
                blockService.sendVersionInfoBlock(session.getUser().getMessengerId());
                intentService.handleDiscover(session);
//                session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
//                blockService.sendSuggestionsBlock(session);
                break;

            case utterance:
                final String utterance = payload.getString("utterance");
                defaultTextHandler.handle(utterance, session);
                break;

            case help:
                intentService.handleHelp(session);
                break;

            case search_suggestions:
                intentService.handleSuggestions(session);
//                session.setSessionState(SessionState.SEARCH_SUGGESTIONS);
//                blockService.sendSuggestionsBlock(session);
                break;

            case most_loved:
                session.setSessionState(SessionState.MOST_LOVED);
                blockService.sendMostLovedBlock(session);
                break;

//            case whats_new:
//                session.setSessionState(SessionState.WHATS_NEW);
//                blockService.sendWhatsNewBlock(session);
//                break;

            case discover:
                intentService.handleDiscover(session);
                break;

            case see_more:
                intentService.handleSeemore(session);
                break;

            case item_more_options:
                handleItemMoreOptions(session, payload);
                break;

            case set_geolocation:
                blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                break;

            case show_subcategories:
                blockService.sendSubcategoriesQuickrepliesBlock(session, 1);
                break;

            case set_subcategory:
                final String subcategoryId = payload.getString("id");
                intentService.handleSetSubcategory(session, subcategoryId);
                break;

            case get_a_summary:
                intentService.handleGetasummary(session);
                break;

            case venues_more_info:
                blockService.sendVenueSummaryBlock(
                    session.getUser().getMessengerId(),
                    payload.getString("venue_id")
                );
                break;

            case venues_show_areas:
                final Integer pageNumber = payload.getInt("pageNumber");
                blockService.sendAreasQuickrepliesBlock(session, pageNumber);
                break;

            case venues_set_neighborhood:
                final String neighborhoodId = payload.getString("neighborhood_id");
                final Neighborhood neighborhood = graffittiService.getNeighborhoodByGraffittiId(neighborhoodId);
                if (neighborhood!=null) {
                    final SessionStateLookingBag bag = session.getSessionStateLookingBag();
                    bag.setGraffittiPageNumber(1);
                    bag.setGeolocation(null);
                    bag.setNeighborhood(neighborhood);
                    intentService.handleFindRestaurants(session);
                } else {
                    blockService.sendErrorBlock(session.getUser());
                }
                break;

            case where_everywhere:
                final SessionStateLookingBag bag = session.getSessionStateLookingBag();
                bag.setGraffittiPageNumber(1);
                bag.setGeolocation(null);
                bag.setNeighborhood(null);
                intentService.handleFindRestaurants(session);
                break;

            case photos:
                intentService.handlePhotos(session);
                break;

            case no_see_at_timeout:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Ok, back to the list of restaurants"
                );
                intentService.handleFindRestaurants(session);
                break;

            case films_find_cinemas:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Find a cinema' is not implemented yet"
                );
                break;

            case cancel:
                intentService.handleCancel(session);
                break;

            case back:
                intentService.handleBack(session);
                break;

            case phone_call:
                final String phoneNumber = payload.getString("phone_number");
                final String venueName = payload.getString("venue_name");
                blockService.sendPhoneCallBlock(
                    session.getUser().getMessengerId(),
                    phoneNumber,
                    venueName
                );
                break;

            case submit_photo:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Please attach one or more photos"
                );
                break;

            case book:
                bookingBeginHandler.handle(session);
                break;

            case submit_review:
                handleSumitReview(session, payload);
                break;

            case temporaly_disabled:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            default:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }

    private void handleItemMoreOptions(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateItemBag bag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = GraffittiType.fromString(payload.getString("item_type"));

        switch (graffittiType) {

            case VENUE:
                bag.setGraffittiType(graffittiType);
                bag.setItemId(payload.getString("item_id"));
                session.setSessionState(SessionState.ITEM);
                intentService.handleSeeItem(session);
                break;

            case EVENT:
            case FILM:
            case PAGE:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, only 'More options' for Venues is available",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;
        }
    }

    private void handleSumitReview(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.getSessionState();
        if (sessionState == SessionState.ITEM) {
            session.setSessionState(SessionState.SUBMITTING_REVIEW);
            final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();
            bag.setSubmittingReviewState(SubmittingReviewState.RATING);
            bag.setRate(null);
            bag.setComment(null);
            blockService.sendSubmittingReviewRateBlock(session.getUser().getMessengerId());
        } else {
            blockService.sendErrorBlock(session.getUser());
        }
    }
}
