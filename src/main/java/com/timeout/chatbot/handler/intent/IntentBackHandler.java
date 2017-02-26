package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntentBackHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final IntentFindVenuesHandler intentFindVenuesHandler;

    @Autowired
    public IntentBackHandler(
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

            case ITEM:
                handleItem(session);
                break;

            case SEARCHING:
            case UNDEFINED:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "I can't go back"
                );
                break;

            case BOOKING:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleItem(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

        if (
            bag.getCategory() == Category.RESTAURANT ||
            bag.getCategory() == Category.HOTEL
        ) {
            intentFindVenuesHandler.fetchAndSend(session);
        }
    }
}
