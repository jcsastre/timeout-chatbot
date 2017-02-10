package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.ErrorBlock;
import com.timeout.chatbot.block.PhotosBlock;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.images.GraffittiImagesResponse;
import com.timeout.chatbot.graffitti.urlbuilder.VenuesUrlBuilder;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateItemBag;
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
    private final ErrorBlock errorBlock;

    @Autowired
    public IntentPhotosHandler(
        MessengerSendClient messengerSendClient,
        PhotosBlock photosBlock,
        RestTemplate restTemplate,
        VenuesUrlBuilder venuesUrlBuilder,
        ErrorBlock errorBlock
    ) {
        this.messengerSendClient = messengerSendClient;
        this.photosBlock = photosBlock;
        this.restTemplate = restTemplate;
        this.venuesUrlBuilder = venuesUrlBuilder;
        this.errorBlock = errorBlock;
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
                    "Sorry, I don't know what photos do you want to see"
                );
                break;

            case BOOKING:
            default:
                errorBlock.send(session.getUser());
                break;
        }
    }

    private void handleStateItem(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE) {
            photosBlock.send(
                session.getUser().getMessengerId(),
                itemBag.getVenue()
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
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
