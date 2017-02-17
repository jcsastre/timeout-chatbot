package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateLookingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntentSeemoreHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final IntentFindRestaurantsHandler findRestaurantsHandler;

    @Autowired
    public IntentSeemoreHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        IntentFindRestaurantsHandler findRestaurantsHandler
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.findRestaurantsHandler = findRestaurantsHandler;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        switch (session.getSessionState()) {

            case SEARCHING:
                handleLooking(session);
                break;

            case BOOKING:
            case UNDEFINED:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleLooking(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateLookingBag bag = session.getSessionStateLookingBag();

        final What what = bag.getWhat();

        if (bag.getReaminingItems() > 0) {
            bag.setGraffittiPageNumber(bag.getGraffittiPageNumber() +1);

            if (what == What.RESTAURANT) {
                findRestaurantsHandler.fetchAndSend(session);
            }
        } else {
            if (what == What.RESTAURANT) {
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "There are no remaining RestaurantsManager"
                );
            }
        }

    }
}
