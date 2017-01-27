package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSeemoreHandler {

    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final IntentFindRestaurantsHandler findRestaurantsHandler;

    @Autowired
    public IntentSeemoreHandler(
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        IntentFindRestaurantsHandler findRestaurantsHandler
    ) {
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.findRestaurantsHandler = findRestaurantsHandler;
    }

    public void handle(
        Session session
    ) {
        switch (session.getSessionState()) {

            case LOOKING:
                handleLooking(session);
                break;

            case BOOKING:
                handleBooking();
                break;

            case UNDEFINED:
            case WELCOMED:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handleLooking(
        Session session
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
        final What what = bag.getWhat();

        if (bag.getReaminingItems() > 0) {
            bag.setGraffittiPageNumber(bag.getGraffittiPageNumber() +1);
            if (what == What.RESTAURANT) {
                findRestaurantsHandler.handleOtherThanBooking(session);
            }
        } else {
            if (what == What.RESTAURANT) {
                messengerSendClientWrapper.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "There are no remaining Restaurants"
                );
            }
        }
    }

    public void handleBooking() {
        //TODO: handleBooking
    }

}
