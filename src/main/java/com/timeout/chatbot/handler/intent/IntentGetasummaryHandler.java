package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.venues.GraffittiVenueResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
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

        switch (session.getSessionState()) {

            case ITEM:
                handleInternal(session);
                break;

            case LOOKING:
            case BOOKING:
            case UNDEFINED:
            case WELCOMED:
            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void handleInternal(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE) {
            final GraffittiVenueResponse graffittiVenueResponse =
                restTemplate.getForObject(
                    venuesUrlBuilder.build(itemBag.getItemId()).toUri(),
                    GraffittiVenueResponse.class
                );

            final GraffittiVenueResponse.Body body = graffittiVenueResponse.getBody();

            String summary = body.getSummary();
            if (summary == null) {
                summary = "Sorry, there is no summary available";
            }

            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                summary,
                quickReplyBuilderHelper.buildForSeeVenueItem()
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, this feature is not implemented yet"
            );
        }
    }
}
