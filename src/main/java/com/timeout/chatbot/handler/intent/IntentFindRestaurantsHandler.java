package com.timeout.chatbot.handler.intent;

import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.graffitti.domain.response.search.page.SearchResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.services.GraffittiService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
import com.timeout.chatbot.session.context.SessionState;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class IntentFindRestaurantsHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;
    private final SearchUrlBuilder searchUrlBuilder;

    private static final String WHAT_RESTAURANTS ="node-7083";

    @Autowired
    public IntentFindRestaurantsHandler(
        RestTemplate restTemplate,
        BlockService blockService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiService graffittiService,
        SearchUrlBuilder searchUrlBuilder
    ) {
        this.restTemplate = restTemplate;
        this.blockService = blockService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiService = graffittiService;
        this.searchUrlBuilder = searchUrlBuilder;
    }

    public void handle(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case WELCOMED:
            case LOOKING:
                handleOtherThanBooking(session, nluParameters);
                break;

            case BOOKING:
                handleBooking();
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handle(
        Session session
    ) {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case WELCOMED:
            case LOOKING:
                handleOtherThanBooking(session);
                break;

            case BOOKING:
                handleBooking();
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    public void handleOtherThanBooking(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) {
        if (
            nluParameters.containsKey("whereUkLondon")
        ) {
            final SessionStateLookingBag bag = session.getSessionStateLookingBag();

            final String where = nluParameters.get("whereUkLondon").getAsString();
            if (
                where.equalsIgnoreCase("nearby") ||
                where.equalsIgnoreCase("near me") ||
                where.equalsIgnoreCase("near of me")
            ) {
                if (bag.getGeolocation() == null) {
                    blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                    return;
                }
            } else {
                bag.setGraffittiWhere(where);
            }
        }

        if (
            nluParameters.containsKey("whatRestaurantsUkLondon")
        ) {
            final SessionStateLookingBag bag = session.getSessionStateLookingBag();

            final String whatId = nluParameters.get("whatRestaurantsUkLondon").getAsString();
            final GraffittiFacetV5Node graffittiFacetV5Node = graffittiService.getGraffittiFacetV5NodeById(whatId);
            bag.setGraffittiFacetV5WhatV5Node(graffittiFacetV5Node);
        }

        handleOtherThanBooking(session);
    }

    public void handleBooking() {
        //TODO: handleBooking
    }

    public void handleOtherThanBooking(
        Session session
    ) {
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();

        bag.setWhat(What.RESTAURANT);

        UrlBuilder urlBuilder = urlBuilderBase(
            bag.getGraffittiFacetV5WhatV5Node(),
            bag.getGraffittiPageNumber()
        );

        final Geolocation geolocation = bag.getGeolocation();
        if (geolocation != null) {
            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.LATITUDE.getValue(),
                        geolocation.getLatitude().toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.LONGITUDE.getValue(),
                        geolocation.getLongitude().toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.RADIUS.getValue(),
                        "1"
                    )
                    .addParameter(
                        GraffittiQueryParameterType.SORT.getValue(),
                        "distance"
                    );
        } else {
            final String where = bag.getGraffittiWhere();

            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.WHERE.getValue(),
                        where
                    );
        }

        System.out.println(urlBuilder.toUrl().toString());

//        ///
//        final SessionStateLookingBag bag = session.getSessionStateLookingBag();
//        final What what = bag.getWhat();
//
//        if (what != What.RESTAURANT) {
//            bag.setWhat(What.RESTAURANT);
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

        session.setSessionState(SessionState.LOOKING);
    }

    private UrlBuilder urlBuilderBase(
        GraffittiFacetV5Node graffittiFacetV5Node,
        Integer pageNumber
    ) {
        if (graffittiFacetV5Node == null) {
            return searchUrlBuilder.buildBase(
                WHAT_RESTAURANTS,
                GraffittiType.VENUE.getValue(),
                pageNumber
            );
        } else {
            return searchUrlBuilder.buildBase(
                graffittiFacetV5Node.getId(),
                GraffittiType.VENUE.getValue(),
                pageNumber
            );
        }
    }

//    private URI buildUri(Session session, SessionStateLookingBag bag) {
//        URI uri = null;
//
//        final SessionContextBag.Geolocation geolocation = bag.getGeolocation();
//
//        if (geolocation == null) {
//            uri =
//                RestaurantsSearchUri.buildNonGeolocatedUri(
//                    bag.getGraffittiPageNumber()
//                );
//        } else {
//            uri =
//                RestaurantsSearchUri.buildGeolocatedUri(
//                    geolocation.getLatitude(),
//                    geolocation.getLongitude(),
//                    bag.getGraffittiPageNumber()
//                );
//
//            messengerSendClientWrapper.sendTextMessage(
//                session.getUser().getMessengerId(),
//                "Looking for Restaurants within 500 meters from the current location. Please, give me a moment."
//            );
//        }
//
//        return uri;
//    }
}
