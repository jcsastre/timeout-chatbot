package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.images.GraffittiImagesResponse;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IntentSeeItem {

    private final MessengerSendClient messengerSendClient;
    private final BlockService blockService;
    private final RestTemplate restTemplate;
    private final VenuesUrlBuilder venuesUrlBuilder;

    @Autowired
    public IntentSeeItem(
        MessengerSendClient messengerSendClient,
        BlockService blockService,
        RestTemplate restTemplate, VenuesUrlBuilder venuesUrlBuilder) {
        this.messengerSendClient = messengerSendClient;
        this.blockService = blockService;
        this.restTemplate = restTemplate;
        this.venuesUrlBuilder = venuesUrlBuilder;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case LOOKING:
            case ITEM:
                final SessionStateItemBag itemBag = session.getSessionStateItemBag();
                final GraffittiType graffittiType = itemBag.getGraffittiType();
                if (graffittiType == GraffittiType.VENUE) {

                    final GraffittiVenueResponse graffittiVenueResponse =
                        restTemplate.getForObject(
                            venuesUrlBuilder.build(itemBag.getItemId()).toUri(),
                            GraffittiVenueResponse.class
                        );

                    final GraffittiImagesResponse graffittiImagesResponse =
                        restTemplate.getForObject(
                            venuesUrlBuilder.buildImages(graffittiVenueResponse.getBody().getId()).toUri(),
                            GraffittiImagesResponse.class
                        );


                    final Venue venue =
                        new Venue(
                            graffittiVenueResponse.getBody(),
                            graffittiImagesResponse.getGraffittiImages()
                        );

                    itemBag.setVenue(venue);

                    blockService.sendSeeVenueItemBlock(
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
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }
}
