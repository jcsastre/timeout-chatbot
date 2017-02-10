package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntentBookHandler {

    private final MessengerSendClient messengerSendClient;
    private final BlockService blockService;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public IntentBookHandler(
        MessengerSendClient messengerSendClient,
        BlockService blockService,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.blockService = blockService;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        switch (session.getSessionState()) {

            case ITEM:
                handleStateItem(session);
                break;

            case UNDEFINED:
            case LOOKING:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't know what you want to book"
                );
                break;

            case BOOKING:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleStateItem(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE) {
            final Venue venue = itemBag.getVenue();

            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, 'Book' feature is not implemented yet",
                quickReplyBuilderHelper.buildForSeeVenueItem(venue)
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, 'Book' feature is not implemented yet"
            );
        }
    }
}
