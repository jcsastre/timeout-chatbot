package com.timeout.chatbot.handler.intent;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Neighborhood neighborhood,
        Geolocation geolocation
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
        switch (session.state) {

            case BOOKING:
                //TODO: Implement cancel
                break;

            case SUBMITTING_REVIEW:
                //TODO: Implement cancel
                break;

            default:
                handleDefault(
                    session,
                    graffittiCategory,
                    graffittiSubcategory,
                    neighborhood,
                    geolocation
                );
                break;
        }
    }

    private void handleDefault(
        Session session,
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Neighborhood neighborhood,
        Geolocation geolocation
    ) {
//        if (where != null) {
//            final SessionStateSearchingBag bag = session.bagSearching;
//
//            if (
//                    where.equalsIgnoreCase("nearby") ||
//                    where.equalsIgnoreCase("near me") ||
//                    where.equalsIgnoreCase("near of me")
//                ) {
//                if (bag.geolocation == null) {
//                    blockService.sendGeolocationAskBlock(session.user.messengerId);
//                } else {
//                    perform(session);
//                }
//            } else {
//                perform(session);
//                //TODO: map text to valid where
//                //bag.setGraffittiWhere(where);
//            }
//        }
    }

//    private void applyNluParameters(
//        Session session,
//        HashMap<String, JsonElement> nluParameters
//    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
//
//        if (
//            nluParameters.containsKey("whereUkLondon")
//        ) {
//            final SessionStateSearchingBag bag = session.bagSearching;
//
//            final String where = nluParameters.get("whereUkLondon").getAsString();
//            if (
//                where.equalsIgnoreCase("nearby") ||
//                where.equalsIgnoreCase("near me") ||
//                where.equalsIgnoreCase("near of me")
//            ) {
//                if (bag.geolocation == null) {
//                    blockService.sendGeolocationAskBlock(session.user.messengerId);
//                } else {
//                    perform(session);
//                }
//            } else {
//                perform(session);
//                //TODO: map text to valid where
//                //bag.setGraffittiWhere(where);
//            }
//        } else {
//            perform(session);
//        }
//    }

    public void fetchAndSendInternal(
        String userMessengerId,
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Integer pageNumber,
        Geolocation geolocation,
        Neighborhood neighborhood,
        Integer reaminingItems
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {
//
//        UrlBuilder urlBuilder =
//            searchUrlBuilder.build(
//                graffittiSubcategory != null ? graffittiSubcategory.graffittiId : graffittiCategory.graffittiId,
//                GraffittiType.VENUE.toValue(),
//                pageNumber
//            );
//
//        if (geolocation != null) {
//            urlBuilder =
//                urlBuilder
//                    .addParameter(
//                        GraffittiQueryParameterType.LATITUDE.getValue(),
//                        geolocation.latitude.toString()
//                    )
//                    .addParameter(
//                        GraffittiQueryParameterType.LONGITUDE.getValue(),
//                        geolocation.longitude.toString()
//                    )
//                    .addParameter(
//                        GraffittiQueryParameterType.RADIUS.getValue(),
//                        "1"
//                    )
//                    .addParameter(
//                        GraffittiQueryParameterType.SORT.getValue(),
//                        "distance"
//                    );
//        } else if (neighborhood != null) {
//            urlBuilder =
//                urlBuilder
//                    .addParameter(
//                        GraffittiQueryParameterType.WHERE.getValue(),
//                        neighborhood.graffitiId
//                    );
//        }
//
//        senderActionsHelper.typingOn(userMessengerId);
//        messengerSendClient.sendTextMessage(
//            userMessengerId,
//            buildMessage(
//                graffittiCategory,
//                graffittiSubcategory,
//                geolocation,
//                neighborhood
//            )
//        );
//
//        senderActionsHelper.typingOn(userMessengerId);
//
//        logger.debug(urlBuilder.toUrl().toString());
//
//        final GraffittiSearchResponse graffittiSearchResponse =
//            restTemplate.getForObject(
//                urlBuilder.toUri(),
//                GraffittiSearchResponse.class
//            );
//
//        if (graffittiSearchResponse.getMeta().getTotalItems() > 0) {
//
//            blockService.getVenuesPageBlock().send(
//                userMessengerId,
//                graffittiSearchResponse.getPageItems(),
//                graffittiCategory.namePlural
//            );
//
//            senderActionsHelper.typingOn(userMessengerId);
//            blockService.getVenuesRemainingBlock().send(
//                userMessengerId,
//                graffittiCategory,
//                graffittiSubcategory,
//                reaminingItems,
//                neighborhood,
//                geolocation
//            );
//        } else {
//            messengerSendClient.sendTextMessage(
//                userMessengerId,
//                "There are not available " + graffittiCategory.namePlural + " for your request."
//            );
//        }
//
//        session.state = SessionState.SEARCHING;
    }

    public void fetchAndSend(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        fetchAndSendInternal(
            session.user.messengerId,
            session.bagSearching.graffittiCategory,
            session.bagSearching.graffittiSubcategory,
            session.bagSearching.pageNumber,
            session.bagSearching.geolocation,
            session.bagSearching.neighborhood,
            session.bagSearching.reaminingItems
        );
    }

    private String buildMessage(
        final GraffittiCategory graffittiCategory,
        final GraffittiSubcategory graffittiSubcategory,
        final Geolocation geolocation,
        final Neighborhood neighborhood
    ) {
//        String msg = "Looking for";
//
//        if (graffittiSubcategory != null) {
//            msg = msg + " " + graffittiSubcategory.name.toLowerCase();
//        }
//
//        msg = msg + " " + graffittiCategory.namePlural.toLowerCase();
//
//        if (geolocation != null) {
//            msg = msg + " near the location you specified";
//        } else if (neighborhood != null) {
//            msg = msg + " at " + neighborhood.name;
//        }
//
//        return msg;

        return  null;
    }
}
