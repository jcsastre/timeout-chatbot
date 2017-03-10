package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.google.gson.JsonElement;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Subcategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
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
public class IntentFindVenuesHandler {

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final SearchUrlBuilder searchUrlBuilder;
    private final SenderActionsHelper senderActionsHelper;

    @Autowired
    public IntentFindVenuesHandler(
        RestTemplate restTemplate,
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        SearchUrlBuilder searchUrlBuilder,
        SenderActionsHelper senderActionsHelper
    ) {
        this.restTemplate = restTemplate;
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.searchUrlBuilder = searchUrlBuilder;
        this.senderActionsHelper = senderActionsHelper;
    }

    public void handle(
        Session session,
        HashMap<String, JsonElement> nluParameters
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.getSessionState()) {

            case SEARCHING:
                applyNluParameters(session, nluParameters);
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

            case SEARCHING:
                fetchAndSend(session);
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

    public void fetchAndSend(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

        final Category category = bag.getCategory();

        UrlBuilder urlBuilder;
        final Subcategory subcategory = bag.getSubcategory();
        if (subcategory != null) {
            urlBuilder = urlBuilderBase(
                subcategory,
                bag.getGraffittiPageNumber()
            );
        } else {
            urlBuilder = urlBuilderBase(
                category,
                bag.getGraffittiPageNumber()
            );
        }

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
            final Neighborhood neighborhood = bag.getNeighborhood();

            if (neighborhood != null) {
                urlBuilder =
                    urlBuilder
                        .addParameter(
                            GraffittiQueryParameterType.WHERE.getValue(),
                            neighborhood.getGraffitiId()
                        );
            }
        }

        senderActionsHelper.typingOn(session.getUser().getMessengerId());
        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            buildMessage(bag)
        );

        senderActionsHelper.typingOn(session.getUser().getMessengerId());
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
                category.getNamePlural()
            );

            senderActionsHelper.typingOn(session.getUser().getMessengerId());
            blockService.sendVenuesRemainingBlock(
                session
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "There are not available " + category.getNamePlural() + " for your request."
            );
        }

        session.setSessionState(SessionState.SEARCHING);
    }

    private String buildMessage(
        SessionStateSearchingBag bag
    ) {
        String msg = "Looking for";

        final Subcategory subcategory = bag.getSubcategory();
        if (subcategory != null) {
            msg = msg + " " + subcategory.getName().toLowerCase();
        }

        final Category category = bag.getCategory();
        msg = msg + " " + category.getNamePlural().toLowerCase();

        final Geolocation geolocation = bag.getGeolocation();
        if (geolocation != null) {
            msg = msg + " near the location you specified";
        } else {
            final Neighborhood neighborhood = bag.getNeighborhood();
            if (neighborhood != null) {
                msg = msg + " at " + neighborhood.getName();
            }
        }

        return msg;
    }

    private UrlBuilder urlBuilderBase(
        Category category,
        Integer pageNumber
    ) {
        return searchUrlBuilder.build(
            category.getGraffittiId(),
            GraffittiType.venue.toString(),
            pageNumber
        );
    }

    private UrlBuilder urlBuilderBase(
        Subcategory subcategory,
        Integer pageNumber
    ) {
        return searchUrlBuilder.build(
            subcategory.getGraffittiId(),
            GraffittiType.venue.toString(),
            pageNumber
        );
    }
}
