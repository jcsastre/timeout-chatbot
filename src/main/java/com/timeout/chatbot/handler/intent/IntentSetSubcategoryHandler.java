package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSetSubcategoryHandler {

    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final IntentFindRestaurantsHandler findRestaurantsHandler;
    private final GraffittiService graffittiService;

    @Autowired
    public IntentSetSubcategoryHandler(
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        IntentFindRestaurantsHandler findRestaurantsHandler,
        GraffittiService graffittiService
    ) {
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.findRestaurantsHandler = findRestaurantsHandler;
        this.graffittiService = graffittiService;
    }

    public void handle(
        Session session,
        String subcategoryId
    ) {
        switch (session.getSessionState()) {

            case LOOKING:
                handleLooking(session, subcategoryId);
                break;

            case BOOKING:
            case UNDEFINED:
            case WELCOMED:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handleLooking(
        Session session,
        String subcategoryId
    ) {

        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
        final GraffittiFacetV4FacetNode categoryNode = graffittiService.findNodeInfacetWhatCategoriesRootNode(subcategoryId);
        bag.setGraffittiWhatCategoryNode(categoryNode);

        findRestaurantsHandler.handleOtherThanBooking(session);

//        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
//        final What what = bag.getWhat();
//
//        if (bag.getReaminingItems() > 0) {
//            bag.setGraffittiPageNumber(bag.getGraffittiPageNumber() +1);
//            if (what == What.RESTAURANT) {
//                findRestaurantsHandler.handleOtherThanBooking(session);
//            }
//        } else {
//            if (what == What.RESTAURANT) {
//                messengerSendClientWrapper.sendTextMessage(
//                    session.getUser().getMessengerId(),
//                    "There are no remaining Restaurants"
//                );
//            }
//        }
    }
}
