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
        switch (session.state) {

            case UNDEFINED:
            case SEARCHING:
            case ITEM:
                if (session.bagItem.graffittiType == GraffittiType.VENUE) {

                    final Venue venue = graffittiService.fetchVenue(session.bagItem.itemId);
                    session.bagItem.venue = venue;

                    seeVenueItemBlock.send(
                        session.user.messengerId,
                        venue
                    );
                } else {
                    messengerSendClient.sendTextMessage(
                        session.user.messengerId,
                        "Sorry, this feature is not implemented yet"
                    );
                }
                break;

            case BOOKING:
            default:
                blockError.send(session.user.messengerId);
                break;
        }
    }
}
