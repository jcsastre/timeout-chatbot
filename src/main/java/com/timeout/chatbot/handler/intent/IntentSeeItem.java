package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentSeeItem {

    private final MessengerSendClient messengerSendClient;
    private final BlockService blockService;

    @Autowired
    public IntentSeeItem(
        MessengerSendClient messengerSendClient,
        BlockService blockService
    ) {
        this.messengerSendClient = messengerSendClient;
        this.blockService = blockService;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case LOOKING:
            case WELCOMED:
            case ITEM:
                final SessionStateItemBag itemBag = session.getSessionStateItemBag();
                final GraffittiType graffittiType = itemBag.getGraffittiType();
                if (graffittiType == GraffittiType.VENUE) {
                    blockService.sendSeeVenueItemBlock(session);
                } else {
                    messengerSendClient.sendTextMessage(
                        session.getUser().getMessengerId(),
                        "Sorry, this feature is not implemented yet"
                    );
                }
                break;

            case BOOKING:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }
}
