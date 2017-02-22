package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
import com.timeout.chatbot.session.state.SessionState;
import io.mikael.urlbuilder.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@Component
public class IntentFindRestaurantsHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final SearchUrlBuilder searchUrlBuilder;

    private static final String WHAT_RESTAURANTS ="node-7083";

    @Autowired
    public IntentFindRestaurantsHandler(
        RestTemplate restTemplate,
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        SearchUrlBuilder searchUrlBuilder
    ) {
        this.restTemplate = restTemplate;
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.searchUrlBuilder = searchUrlBuilder;
    }

    public void handle(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case SEARCHING:
                applyNluParameters(session, nluParameters);
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
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.getSessionState()) {

            case UNDEFINED:
            case SEARCHING:
                fetchAndSend(session);
                break;

            case BOOKING:
                handleBooking();
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }

    private void applyNluParameters(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        if (
            nluParameters.containsKey("whereUkLondon")
        ) {
            final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

            final String where = nluParameters.get("whereUkLondon").getAsString();
            if (
                where.equalsIgnoreCase("nearby") ||
                where.equalsIgnoreCase("near me") ||
                where.equalsIgnoreCase("near of me")
            ) {
                if (bag.getGeolocation() == null) {
                    blockService.sendGeolocationAskBlock(session.getUser().getMessengerId());
                } else {
                    handle(session);
                }
            } else {
                handle(session);
                //TODO: map text to valid where
                //bag.setGraffittiWhere(where);
            }
        } else {
            handle(session);
        }
    }

    private void handleBooking() {
        //TODO: handleBooking
    }

    public void fetchAndSend(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

        UrlBuilder urlBuilder = urlBuilderBase(
            bag.getGraffittiWhatCategoryNode(),
            bag.getGraffittiPageNumber()
        );

        String msg = "Looking for restaurants";

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

            msg = "Looking for restaurants near the location you specified";
        } else {
            final Neighborhood neighborhood = bag.getNeighborhood();

            if (neighborhood != null) {
                urlBuilder =
                    urlBuilder
                        .addParameter(
                            GraffittiQueryParameterType.WHERE.getValue(),
                            neighborhood.getGraffitiId()
                        );

                msg = "Looking for restaurants at "+ neighborhood.getName();
            }
        }

        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            msg
        );

        System.out.println(urlBuilder.toUrl().toString());

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                urlBuilder.toUri(),
                GraffittiSearchResponse.class
            );

        if (graffittiSearchResponse.getMeta().getTotalItems() > 0) {

            bag.setReaminingItems(graffittiSearchResponse.getRemainingItems());

            blockService.sendVenuesPageBlock(
                session,
                graffittiSearchResponse.getPageItems(),
                "RestaurantsManager"
            );

            blockService.sendVenuesRemainingBlock(
                session
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "There are not available restaurants for your request."
            );
        }

        session.setSessionState(SessionState.SEARCHING);
    }

    private UrlBuilder urlBuilderBase(
        GraffittiFacetV4FacetNode graffittiFacetV4Node,
        Integer pageNumber
    ) {
        if (graffittiFacetV4Node == null) {
            return searchUrlBuilder.build(
                WHAT_RESTAURANTS,
                GraffittiType.VENUE.toString(),
                pageNumber
            );
        } else {
            return searchUrlBuilder.build(
                graffittiFacetV4Node.getId(),
                GraffittiType.VENUE.toString(),
                pageNumber
            );
        }
    }
}
