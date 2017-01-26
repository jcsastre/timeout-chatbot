package com.timeout.chatbot.handler.intent;

import com.timeout.chatbot.graffitti.domain.GraffittiWhat;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.uri.RestaurantsSearchUri;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionContextBag;
import com.timeout.chatbot.session.SessionStateLookingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class IntentFindRestaurantsHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public IntentFindRestaurantsHandler(
        RestTemplate restTemplate,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.restTemplate = restTemplate;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void handle(
        Session session
    ) {

        switch (session.getSessionState()) {

            case UNDEFINED:
                break;

            case WELCOMED:
                break;

            case LOOKING:
                break;

            case BOOKING:
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handle(
        Session session
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
        final GraffittiWhat what = bag.getGraffittiWhat();

        if (what != GraffittiWhat.RESTAURANT) {
            bag.setGraffittiWhat(GraffittiWhat.RESTAURANT);
            bag.setGraffittiPageNumber(1);
        } else {
            Integer pageNumber = bag.getGraffittiPageNumber();
            if (pageNumber != null) {
                pageNumber++;
                bag.setGraffittiPageNumber(pageNumber);;
            } else {
                bag.setGraffittiPageNumber(1);
            }
        }

        final URI uri = buildUri(session, bag);

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                uri,
                SearchResponse.class
            );

        if (searchResponse.getMeta().getTotalItems() > 0) {
            bag.setReaminingItems(searchResponse.getRemainingItems());

            blockService.sendVenuesPageBlock(
                session,
                searchResponse.getPageItems(),
                "Restaurants"
            );

            blockService.sendVenuesRemainingBlock(
                session.getUser().getMessengerId(),
                bag.getReaminingItems(),
                bag.getGeolocation() != null,
                "Restaurants"
            );
        } else {
            messengerSendClientWrapper.sendTextMessage(
                session.getUser().getMessengerId(),
                "There are not available Restaurants for your request."
            );
        }
    }

    public void handleNearby(
        Session session
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();

        if (bag.getGeolocation() == null) {
            blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
        } else {
            handle(session);
        }
    }

    private URI buildUri(Session session, SessionStateLookingBag bag) {
        URI uri = null;

        final SessionContextBag.Geolocation geolocation = bag.getGeolocation();

        if (geolocation == null) {
            uri =
                RestaurantsSearchUri.buildNonGeolocatedUri(
                    bag.getGraffittiPageNumber()
                );
        } else {
            uri =
                RestaurantsSearchUri.buildGeolocatedUri(
                    geolocation.getLatitude(),
                    geolocation.getLongitude(),
                    bag.getGraffittiPageNumber()
                );

            messengerSendClientWrapper.sendTextMessage(
                session.getUser().getMessengerId(),
                "Looking for Restaurants within 500 meters from the current location. Please, give me a moment."
            );
        }

        return uri;
    }
}
