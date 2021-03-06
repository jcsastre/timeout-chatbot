package com.timeout.chatbot.action;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockItem;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SeeItemAction {

    private final MessengerSendClient messengerSendClient;
    private final GraffittiService graffittiService;
    private final BlockItem blockItem;

    @Autowired
    public SeeItemAction(
        MessengerSendClient messengerSendClient,
        GraffittiService graffittiService,
        BlockItem blockItem
    ) {
        this.messengerSendClient = messengerSendClient;
        this.graffittiService = graffittiService;
        this.blockItem = blockItem;
    }

    public void perform(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        if (session.bagItem.graffittiType == GraffittiType.VENUE) {

            final Venue venue = graffittiService.fetchVenue(session.bagItem.itemId);
            session.bagItem.venue = venue;

            blockItem.send(
                session.user.messengerId,
                venue
            );
        } else {

            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "Sorry, this feature is not implemented yet"
            );
        }
    }
}
