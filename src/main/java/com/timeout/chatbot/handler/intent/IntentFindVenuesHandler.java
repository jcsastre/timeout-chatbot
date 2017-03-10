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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@Component
public class IntentFindVenuesHandler {

    private static final Logger logger = LoggerFactory.getLogger(IntentFindVenuesHandler.class);

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
        switch (session.state) {

            case SEARCHING:
                applyNluParameters(session, nluParameters);
                break;

            default:
                blockService.sendErrorBlock(session.user);
                break;
        }
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.state) {

            case SEARCHING:
                fetchAndSend(session);
                break;

            default:
                blockService.sendErrorBlock(session.user);
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
            final SessionStateSearchingBag bag = session.stateSearchingBag;

            final String where = nluParameters.get("whereUkLondon").getAsString();
            if (
                where.equalsIgnoreCase("nearby") ||
                where.equalsIgnoreCase("near me") ||
                where.equalsIgnoreCase("near of me")
            ) {
                if (bag.geolocation == null) {
                    blockService.sendGeolocationAskBlock(session.user.messengerId);
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

        final SessionStateSearchingBag bag = session.stateSearchingBag;

        final Category category = bag.category;

        UrlBuilder urlBuilder;
        final Subcategory subcategory = bag.subcategory;
        if (subcategory != null) {
            urlBuilder = urlBuilderBase(
                subcategory,
                bag.graffittiPageNumber
            );
        } else {
            urlBuilder = urlBuilderBase(
                category,
                bag.graffittiPageNumber
            );
        }

        final Geolocation geolocation = bag.geolocation;
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
            final Neighborhood neighborhood = bag.neighborhood;

            if (neighborhood != null) {
                urlBuilder =
                    urlBuilder
                        .addParameter(
                            GraffittiQueryParameterType.WHERE.getValue(),
                            neighborhood.getGraffitiId()
                        );
            }
        }

        senderActionsHelper.typingOn(session.user.messengerId);
        messengerSendClient.sendTextMessage(
            session.user.messengerId,
            buildMessage(bag)
        );

        senderActionsHelper.typingOn(session.user.messengerId);

        logger.debug(urlBuilder.toUrl().toString());

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                urlBuilder.toUri(),
                GraffittiSearchResponse.class
            );

        if (graffittiSearchResponse.getMeta().getTotalItems() > 0) {

            bag.reaminingItems = graffittiSearchResponse.getRemainingItems();

            blockService.sendVenuesPageBlock(
                session,
                graffittiSearchResponse.getPageItems(),
                category.getNamePlural()
            );

            senderActionsHelper.typingOn(session.user.messengerId);
            blockService.sendVenuesRemainingBlock(
                session
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.user.messengerId,
                "There are not available " + category.getNamePlural() + " for your request."
            );
        }

        session.state = SessionState.SEARCHING;
    }

    private String buildMessage(
        SessionStateSearchingBag bag
    ) {
        String msg = "Looking for";

        final Subcategory subcategory = bag.subcategory;
        if (subcategory != null) {
            msg = msg + " " + subcategory.getName().toLowerCase();
        }

        final Category category = bag.category;
        msg = msg + " " + category.getNamePlural().toLowerCase();

        final Geolocation geolocation = bag.geolocation;
        if (geolocation != null) {
            msg = msg + " near the location you specified";
        } else {
            final Neighborhood neighborhood = bag.neighborhood;
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
