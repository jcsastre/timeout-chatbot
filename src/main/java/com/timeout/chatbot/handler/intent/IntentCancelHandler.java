package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentCancelHandler {

    private final BlockService blockService;

    @Autowired
    public IntentCancelHandler(
        BlockService blockService
    ) {
        this.blockService = blockService;
    }

    public void handle(
        Session session
    ) {
        switch (session.state) {

            case SEARCHING:
                handleLooking(session);
                break;

            case BOOKING:
                //TODO
                break;

            case UNDEFINED:
                //TODO
                break;

            default:
                blockService.getError().send(session.user.messengerId);
                break;
        }
    }

    private void handleLooking(
        Session session
    ) {
//        final SessionStateSearchingBag bag = session.bagSearching;
////        final What what = bag.getWhat();
//
//        if (what == What.RESTAURANTS) {
//            blockService.sendVenuesRemainingBlock(session);
//        }
    }
}
