package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IntentGetasummaryHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final RestTemplate restTemplate;
    private final VenuesUrlBuilder venuesUrlBuilder;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public IntentGetasummaryHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        RestTemplate restTemplate,
        VenuesUrlBuilder venuesUrlBuilder,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.restTemplate = restTemplate;
        this.venuesUrlBuilder = venuesUrlBuilder;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        switch (session.state) {

            case ITEM:
                handleStateItem(session);
                break;

            case SEARCHING:
            case BOOKING:
            case UNDEFINED:
            default:
                blockService.sendErrorBlock(session.user);
                break;
        }
    }

    private void handleStateItem(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.stateItemBag;

        final GraffittiType graffittiType = itemBag.graffittiType;
        if (graffittiType == GraffittiType.venue) {

            final Venue venue = itemBag.venue;

            String summary = venue.getSummary();
            if (summary == null) {
                summary = "Sorry, there is no summary available";
            }

            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                summary,
                quickReplyBuilderHelper.buildForSeeVenueItem(venue)
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "Sorry, this feature is not implemented yet"
            );
        }
    }
}
