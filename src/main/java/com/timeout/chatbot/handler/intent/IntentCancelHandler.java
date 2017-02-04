package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
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
        switch (session.getSessionState()) {

            case LOOKING:
                handleLooking(session);
                break;

            case BOOKING:
                //TODO
                break;

            case UNDEFINED:
                //TODO
                break;

            case WELCOMED:
                //TODO
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleLooking(
        Session session
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
        final What what = bag.getWhat();

        if (what == What.RESTAURANT) {
            blockService.sendVenuesRemainingBlock(session);
        }
    }
}
