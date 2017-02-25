package com.timeout.chatbot.handler.states.searching;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.AreasQuickrepliesBlock;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.SubcategoriesQuickrepliesBlock;
import com.timeout.chatbot.block.VenuesRemainingBlock;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentFindVenuesHandler;
import com.timeout.chatbot.handler.intent.IntentSeeItem;
import com.timeout.chatbot.handler.intent.IntentSetSubcategoryHandler;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchingStatePayloadHandler {

    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;
    private final AreasQuickrepliesBlock areasQuickrepliesBlock;
    private final SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock;
    private final VenuesRemainingBlock venuesRemainingBlock;
    private final IntentFindVenuesHandler intentFindVenuesHandler;
    private final IntentSetSubcategoryHandler intentSetSubcategoryHandler;
    private final IntentSeeItem intentSeeItem;
    private final BlockError blockError;

    @Autowired
    public SearchingStatePayloadHandler(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService,
        AreasQuickrepliesBlock areasQuickrepliesBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        IntentFindVenuesHandler intentFindVenuesHandler,
        IntentSetSubcategoryHandler intentSetSubcategoryHandler,
        IntentSeeItem intentSeeItem, BlockError blockError
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
        this.areasQuickrepliesBlock = areasQuickrepliesBlock;
        this.subcategoriesQuickrepliesBlock = subcategoriesQuickrepliesBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.intentFindVenuesHandler = intentFindVenuesHandler;
        this.intentSetSubcategoryHandler = intentSetSubcategoryHandler;
        this.intentSeeItem = intentSeeItem;
        this.blockError = blockError;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case item_more_options:
                handleItemMoreOptions(session, payload);
                break;

            case venues_show_areas:
                handleVenuesShowAreas(session, payload);
                break;

            case show_subcategories:
                handleShowSubcategories(session, payload);
                break;

            case cancel:
                venuesRemainingBlock.send(session);
                break;

            case venues_set_neighborhood:
                handleVenuesSetNeighborhood(session, payload);
                break;

            case set_subcategory:
                handleSetSubcategory(session, payload);
                break;

            case where_everywhere:
                handleWhereEverywhere(session);
                break;

            default:
                blockError.send(session.getUser());
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
                intentSeeItem.handle(session);
                break;

            case EVENT:
            case FILM:
            case PAGE:
//                messengerSendClient.sendTextMessage(
//                    session.getUser().getMessengerId(),
//                    "Sorry, only 'More options' for Venues is available",
//                    quickReplyBuilderForCurrentSessionState.build(session)
//                );
                break;
        }
    }

    private void handleSetSubcategory(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final String subcategoryId = payload.getString("subcategory_id");
        intentSetSubcategoryHandler.handle(session, subcategoryId);
    }

    private void handleShowSubcategories(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        subcategoriesQuickrepliesBlock.send(
            session,
            payload.getInt("pageNumber")
        );
    }

    private void handleVenuesShowAreas(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        areasQuickrepliesBlock.send(
            session,
            payload.getInt("pageNumber")
        );
    }

    private void handleWhereEverywhere(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();
        bag.setNeighborhood(null);
        intentFindVenuesHandler.handle(session);
    }

    public void handleVenuesSetNeighborhood(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final String neighborhoodId = payload.getString("neighborhood_id");
        final Neighborhood neighborhood = graffittiService.getNeighborhoodByGraffittiId(neighborhoodId);
        if (neighborhood!=null) {
            final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();
            bag.setNeighborhood(neighborhood);
            intentFindVenuesHandler.handle(session);
        } else {
            blockError.send(session.getUser());
        }
    }
}
