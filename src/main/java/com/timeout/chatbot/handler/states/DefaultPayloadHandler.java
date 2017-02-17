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
import com.timeout.chatbot.handler.states.booking.BookingStatePayloadHandler;
import com.timeout.chatbot.handler.states.item.ItemStatePayloadHandler;
import com.timeout.chatbot.handler.states.submittingreview.SubmittingReviewStatePayloadHandler;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateLookingBag;
import com.timeout.chatbot.session.state.SessionState;
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
    private final SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler;
    private final BookingStatePayloadHandler bookingStatePayloadHandler;
    private final ItemStatePayloadHandler itemStatePayloadHandler;

    @Autowired
    public DefaultPayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        DefaultTextHandler defaultTextHandler,
        GraffittiService graffittiService,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState,
        SubmittingReviewStatePayloadHandler submittingReviewStatePayloadHandler,
        BookingStatePayloadHandler bookingStatePayloadHandler,
        ItemStatePayloadHandler itemStatePayloadHandler) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.defaultTextHandler = defaultTextHandler;
        this.graffittiService = graffittiService;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
        this.submittingReviewStatePayloadHandler = submittingReviewStatePayloadHandler;
        this.bookingStatePayloadHandler = bookingStatePayloadHandler;
        this.itemStatePayloadHandler = itemStatePayloadHandler;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        if (payloadType == PayloadType.start_over) {

            session.reset();
            blockService.sendWelcomeBackBlock(session.getUser());
            blockService.sendVersionInfoBlock(session.getUser().getMessengerId());
            intentService.handleDiscover(session);

        } else {

            final SessionState sessionState = session.getSessionState();

            try {
                switch (sessionState) {

                    case ITEM:
                        itemStatePayloadHandler.handle(session, payload);
                        break;

                    case SUBMITTING_REVIEW:
                        submittingReviewStatePayloadHandler.handle(session, payload);
                        break;

                    case BOOKING:
                        bookingStatePayloadHandler.handle(session, payload);
                        break;

                    default:
                        handleInternal(session, payload);
                }
            } catch (Exception e) {
                e.printStackTrace();
                blockService.sendErrorBlock(session.getUser());
            }
        }
    }

    private void handleInternal(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                blockService.sendWelcomeFirstTimeBlock(session.getUser());
                blockService.sendVersionInfoBlock(session.getUser().getMessengerId());
                intentService.handleDiscover(session);
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
}