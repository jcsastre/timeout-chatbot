package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.SeeVenueItemBlock;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IntentSeeItem {

    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;
    private final SeeVenueItemBlock seeVenueItemBlock;
    private final BlockError blockError;

    @Autowired
    public IntentSeeItem(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService,
        SeeVenueItemBlock seeVenueItemBlock,
        BlockError blockError
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
        this.seeVenueItemBlock = seeVenueItemBlock;
        this.blockError = blockError;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case SEARCHING:
            case ITEM:
                final SessionStateItemBag itemBag = session.getSessionStateItemBag();
                final GraffittiType graffittiType = itemBag.getGraffittiType();
                if (graffittiType == GraffittiType.venue) {

                    final Venue venue = graffittiService.fetchVenue(itemBag.getItemId());
                    itemBag.setVenue(venue);

                    seeVenueItemBlock.send(
                        session.getUser().getMessengerId(),
                        venue
                    );
                } else {
                    messengerSendClient.sendTextMessage(
                        session.getUser().getMessengerId(),
                        "Sorry, this feature is not implemented yet"
                    );
                }
                break;

            case BOOKING:
            default:
                blockError.send(session.getUser().getMessengerId());
                break;
        }
    }
}
