package com.timeout.chatbot.handler.intent;

import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.nlu.NluResult;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.GraffittiWhat;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import com.timeout.chatbot.graffitti.uri.RestaurantsSearchUri;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionContextBag;
import com.timeout.chatbot.session.SessionStateLookingBag;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

@Component
public class IntentFindRestaurantsHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    private static final String WHAT_RESTAURANTS ="node-7083";

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
        Session session,
        NluResult nluResult
    ) {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case WELCOMED:
            case LOOKING:
                handleOtherThanBooking(session, nluResult);
                break;

            case BOOKING:
                handleBooking();
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handleBooking() {
        //TODO: handleBooking
    }

    public void handleOtherThanBooking(
        Session session,
        NluResult nluResult
    ) {

        final HashMap<String, JsonElement> nluParameters = nluResult.getParameters();

        final UrlBuilder urlBuilder = urlBuilderBase(nluParameters);

        if (nluParameters.get("whereUkLondon").getAsString() !=null) {

            final String whereUkLondon = nluParameters.get("whereUkLondon").getAsString();
            if (
                whereUkLondon.equalsIgnoreCase("nearby") ||
                whereUkLondon.equalsIgnoreCase("near me") ||
                whereUkLondon.equalsIgnoreCase("near of me")
            ) {
                final SessionStateLookingBag bag = session.getSessionStateLookingBag();
                if (bag.getGeolocation() == null) {
                    blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                } else {
                    //TODO: update urlBuilder
                }
            } else {
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.WHERE.getValue(),
                        whereUkLondon
                    );
            }
        }


//        ///
//        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
//        final GraffittiWhat what = bag.getGraffittiWhat();
//
//        if (what != GraffittiWhat.RESTAURANT) {
//            bag.setGraffittiWhat(GraffittiWhat.RESTAURANT);
//            bag.setGraffittiPageNumber(1);
//        } else {
//            Integer pageNumber = bag.getGraffittiPageNumber();
//            if (pageNumber != null) {
//                pageNumber++;
//                bag.setGraffittiPageNumber(pageNumber);;
//            } else {
//                bag.setGraffittiPageNumber(1);
//            }
//        }
//
//        final URI uri = buildUri(session, bag);

        final SearchResponse searchResponse =
            restTemplate.getForObject(
                urlBuilder.toUri(),
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

    private UrlBuilder urlBuilderBase(
        HashMap<String, JsonElement> nluParameters
    ) {
        final String what = nluParameters.get("whatRestaurantsUkLondon").getAsString();

        if (what == null) {
            return SearchUrlBuilder.buildBase(
                WHAT_RESTAURANTS,
                GraffittiType.VENUE.getValue(),
                1
            );
        } else {
            return SearchUrlBuilder.buildBase(
                what,
                GraffittiType.VENUE.getValue(),
                1
            );
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
