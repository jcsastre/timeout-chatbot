package com.timeout.chatbot.handler;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import com.timeout.chatbot.session.SessionStateLookingBag;
import com.timeout.chatbot.session.context.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayloadHandler {

    private final IntentService intentService;
    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final TextHandler textHandler;
    private final GraffittiService graffittiService;

    @Autowired
    public PayloadHandler(
        IntentService intentService,
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        TextHandler textHandler,
        GraffittiService graffittiService
    ) {
        this.intentService = intentService;
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.textHandler = textHandler;
        this.graffittiService = graffittiService;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case get_started:
                blockService.sendWelcomeFirstTimeBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
                break;

            case utterance:
                final String utterance = payload.getString("utterance");
                textHandler.handle(utterance, session);
                break;

            case help:
                intentService.handleHelp(session);
                break;

            case suggestions:
                intentService.handleSuggestions(session);
                break;

            case discover:
                intentService.handleDiscover(session);
                break;

            case most_loved:
                blockService.sendMostLovedBlock(session.getUser());
                break;

            case start_over:
                session.reset();
                blockService.sendWelcomeBackBlock(session.getUser());
                session.setSessionState(SessionState.WELCOMED);
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
                blockService.sendNeighborhoodsQuickrepliesBlock(session, pageNumber);
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

            case venues_book:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, booking is not implemented yet"
                );
                blockService.sendVenuesRemainingBlock(session);
                break;

            case no_see_at_timeout:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Ok, back to the list of restaurants"
                );
                intentService.handleFindRestaurants(session);
                break;

//            case utterance:
//                final String utterance = payload.getString("utterance");
//                //TODO:
//                //onUndefinedStateTextHandler.handle(utterance, session);
//                break;

            case films_find_cinemas:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, 'Find a cinema' is not implemented yet"
                );
                break;

            case cancel:
                intentService.handleCancel(session);
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
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag bag = session.getSessionStateItemBag();
        bag.setGraffittiType(GraffittiType.fromString(payload.getString("item_type")));
        bag.setItemId(payload.getString("item_id"));
        session.setSessionState(SessionState.ITEM);

        intentService.handleSeeItem(session);
    }
}
