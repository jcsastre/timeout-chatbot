package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Subcategory;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntentSetSubcategoryHandler {

    private final BlockService blockService;
    private final IntentFindVenuesHandler findRestaurantsHandler;

    @Autowired
    public IntentSetSubcategoryHandler(
        BlockService blockService,
        IntentFindVenuesHandler findRestaurantsHandler
    ) {
        this.blockService = blockService;
        this.findRestaurantsHandler = findRestaurantsHandler;
    }

    public void handle(
        Session session,
        String subcategoryId
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.state) {

            case SEARCHING:
                handleLooking(session, subcategoryId);
                break;

            default:
                blockService.sendErrorBlock(session.user);
                break;
        }
    }

    public void handleLooking(
        Session session,
        String subcategoryId
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.stateSearchingBag;

        final Category category = bag.category;
        final Subcategory subcategory = category.findSubcategoryByGraffittiId(subcategoryId);

        bag.subcategory = subcategory;

        findRestaurantsHandler.fetchAndSend(session);

//        final SessionStateSearchingBag bag = session.stateSearchingBag;
//        final What what = bag.getWhat();
//
//        if (bag.getReaminingItems() > 0) {
//            bag.setGraffittiPageNumber(bag.getGraffittiPageNumber() +1);
//            if (what == What.RESTAURANTS) {
//                findRestaurantsHandler.applyNluParameters(session);
//            }
//        } else {
//            if (what == What.RESTAURANTS) {
//                messengerSendClientWrapper.sendTextMessage(
//                    session.user.messengerId,
//                    "There are no remaining RestaurantsManager"
//                );
//            }
//        }
    }
}
