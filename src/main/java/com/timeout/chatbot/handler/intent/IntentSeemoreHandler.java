package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntentSeemoreHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final IntentFindVenuesHandler intentFindVenuesHandler;

    @Autowired
    public IntentSeemoreHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        IntentFindVenuesHandler intentFindVenuesHandler
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.intentFindVenuesHandler = intentFindVenuesHandler;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        switch (session.getSessionState()) {

            case SEARCHING:
                handleSearching(session);
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleSearching(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

        if (bag.getReaminingItems() > 0) {
            bag.setGraffittiPageNumber(bag.getGraffittiPageNumber() +1);
            intentFindVenuesHandler.fetchAndSend(session);
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "There are no remaining " + bag.getCategory().getNamePlural()
            );
        }
    }
}
