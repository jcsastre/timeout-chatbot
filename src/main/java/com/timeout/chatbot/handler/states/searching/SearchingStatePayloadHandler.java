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

    public SearchingStatePayloadHandler(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService,
        AreasQuickrepliesBlock areasQuickrepliesBlock,
        SubcategoriesQuickrepliesBlock subcategoriesQuickrepliesBlock,
        VenuesRemainingBlock venuesRemainingBlock,
        IntentSetSubcategoryHandler intentSetSubcategoryHandler,
        IntentSeeItem intentSeeItem,
        BlockError blockError,
        IntentFindVenuesHandler intentFindVenuesHandler
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
        this.areasQuickrepliesBlock = areasQuickrepliesBlock;
        this.subcategoriesQuickrepliesBlock = subcategoriesQuickrepliesBlock;
        this.venuesRemainingBlock = venuesRemainingBlock;
        this.intentSetSubcategoryHandler = intentSetSubcategoryHandler;
        this.intentSeeItem = intentSeeItem;
        this.blockError = blockError;
        this.intentFindVenuesHandler = intentFindVenuesHandler;
    }


    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case searching_SeeMore:
                handleSeeMore(session);
                break;

            case searching_ItemMoreOptions:
                handleItemMoreOptions(session, payload);
                break;

            case searching_VenuesShowAreas:
                handleVenuesShowAreas(session, payload);
                break;

            case searching_ShowSubcategories:
                handleShowSubcategories(session, payload);
                break;

            case cancel:
                venuesRemainingBlock.send(session);
                break;

            case searching_VenuesSetNeighborhood:
                handleVenuesSetNeighborhood(session, payload);
                break;

            case searching_SetSubcategory:
                handleSetSubcategory(session, payload);
                break;

            case searching_WhereEverywhere:
                handleWhereEverywhere(session);
                break;

            default:
                blockError.send(session.user.messengerId);
                break;
        }
    }

    private void handleItemMoreOptions(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

//        final SessionStateItemBag itemBag = session.stateItemBag;
        session.stateItemBag = new SessionStateItemBag();

        final GraffittiType graffittiType = GraffittiType.fromTypeAsString(payload.getString("item_type"));

        switch (graffittiType) {

            case venue:
                session.stateItemBag.graffittiType = graffittiType;
                session.stateItemBag.itemId = payload.getString("item_id");
                session.state = SessionState.ITEM;
                intentSeeItem.handle(session);
                break;

            case event:
            case film:
            case page:
//                messengerSendClient.sendTextMessage(
//                    session.user.messengerId,
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

        session.stateSearchingBag.graffittiPageNumber = 1;
        session.stateSearchingBag.geolocation = null;
        session.stateSearchingBag.neighborhood = null;

        intentFindVenuesHandler.handle(session);
    }

    private void handleVenuesSetNeighborhood(
        Session session,
        JSONObject payload
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final String neighborhoodId = payload.getString("neighborhood_id");
        final Neighborhood neighborhood = graffittiService.getNeighborhoodByGraffittiId(neighborhoodId);
        if (neighborhood!=null) {
            session.stateSearchingBag.graffittiPageNumber = 1;
            session.stateSearchingBag.geolocation = null;
            session.stateSearchingBag.neighborhood = neighborhood;

            intentFindVenuesHandler.handle(session);
        } else {
            blockError.send(session.user.messengerId);
        }
    }

    private void handleSeeMore(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final SessionStateSearchingBag bag = session.stateSearchingBag;

        if (bag.reaminingItems > 0) {
            bag.graffittiPageNumber = bag.graffittiPageNumber +1;
            intentFindVenuesHandler.fetchAndSend(session);
        } else {
            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "There are no remaining " + bag.category.getNamePlural()
            );
        }

    }
}
