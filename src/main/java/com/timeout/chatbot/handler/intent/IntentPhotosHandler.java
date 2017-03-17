package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.PhotosBlock;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.images.GraffittiImagesResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class IntentPhotosHandler {

    private final MessengerSendClient messengerSendClient;
    private final PhotosBlock photosBlock;
    private final RestTemplate restTemplate;
    private final VenuesUrlBuilder venuesUrlBuilder;
    private final BlockError blockError;

    @Autowired
    public IntentPhotosHandler(
        MessengerSendClient messengerSendClient,
        PhotosBlock photosBlock,
        RestTemplate restTemplate,
        VenuesUrlBuilder venuesUrlBuilder,
        BlockError blockError
    ) {
        this.messengerSendClient = messengerSendClient;
        this.photosBlock = photosBlock;
        this.restTemplate = restTemplate;
        this.venuesUrlBuilder = venuesUrlBuilder;
        this.blockError = blockError;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        switch (session.state) {

            case ITEM:
                handleStateItem(session);
                break;

            case UNDEFINED:
            case SEARCHING:
                messengerSendClient.sendTextMessage(
                    session.user.messengerId,
                    "Sorry, I don't know what item_Photos do you want to see"
                );
                break;

            case BOOKING:
            default:
                blockError.send(session.user.messengerId);
                break;
        }
    }

    private void handleStateItem(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.bagItem;

        final GraffittiType graffittiType = itemBag.graffittiType;
        if (graffittiType == GraffittiType.VENUE) {
            photosBlock.send(
                session.user.messengerId,
                itemBag.venue
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "Sorry, 'Photos' feature is not implemented yet"
            );
        }
    }

    private List<GraffittiImage> getImagesForVenue(
        String venueId
    ) {

        final GraffittiImagesResponse graffittiImagesResponse =
            restTemplate.getForObject(
                venuesUrlBuilder.buildImages(venueId).toUri(),
                GraffittiImagesResponse.class
            );

        return graffittiImagesResponse.getGraffittiImages();
    }
}
