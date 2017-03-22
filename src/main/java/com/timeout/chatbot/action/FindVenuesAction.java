package com.timeout.chatbot.action;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiQueryParameterType;
import com.timeout.chatbot.graffitti.urlbuilder.SearchUrlBuilder;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import io.mikael.urlbuilder.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class FindVenuesAction {

    private static final Logger logger = LoggerFactory.getLogger(FindVenuesAction.class);

    private final RestTemplate restTemplate;
    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final SearchUrlBuilder searchUrlBuilder;
    private final SenderActionsHelper senderActionsHelper;

    @Autowired
    public FindVenuesAction(
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

    public void find(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        findInternal(
            session.user.messengerId,
            session.bagSearching.graffittiCategory,
            session.bagSearching.graffittiSubcategory,
            session.bagSearching.pageNumber,
            session.bagSearching.geolocation,
            session.bagSearching.neighborhood,
            session.bagSearching.reaminingItems
        );
    }

    public void findInternal(
        String userMessengerId,
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Integer pageNumber,
        Geolocation geolocation,
        Neighborhood neighborhood,
        Integer reaminingItems
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        UrlBuilder urlBuilder =
            searchUrlBuilder.build(
                graffittiSubcategory != null ? graffittiSubcategory.graffittiId : graffittiCategory.graffittiId,
                GraffittiType.VENUE.toValue(),
                pageNumber
            );

        if (geolocation != null) {
            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.LATITUDE.getValue(),
                        geolocation.latitude.toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.LONGITUDE.getValue(),
                        geolocation.longitude.toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.RADIUS.getValue(),
                        "1"
                    )
                    .addParameter(
                        GraffittiQueryParameterType.SORT.getValue(),
                        "distance"
                    );
        } else if (neighborhood != null) {
            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.WHERE.getValue(),
                        neighborhood.graffitiId
                    );
        }

        senderActionsHelper.typingOn(userMessengerId);
        messengerSendClient.sendTextMessage(
            userMessengerId,
            buildMessage(
                graffittiCategory,
                graffittiSubcategory,
                geolocation,
                neighborhood
            )
        );

        senderActionsHelper.typingOn(userMessengerId);

        logger.debug(urlBuilder.toUrl().toString());

        final GraffittiSearchResponse graffittiSearchResponse =
            restTemplate.getForObject(
                urlBuilder.toUri(),
                GraffittiSearchResponse.class
            );

        if (graffittiSearchResponse.getMeta().getTotalItems() > 0) {

            blockService.getVenuesPageBlock().send(
                userMessengerId,
                graffittiSearchResponse.getPageItems(),
                graffittiCategory.namePlural
            );

            senderActionsHelper.typingOn(userMessengerId);
            blockService.getVenuesRemainingBlock().send(
                userMessengerId,
                graffittiCategory,
                graffittiSubcategory,
                reaminingItems,
                neighborhood,
                geolocation
            );
        } else {
            messengerSendClient.sendTextMessage(
                userMessengerId,
                "There are not available " + graffittiCategory.namePlural + " for your request."
            );
        }
    }

    private String buildMessage(
        final GraffittiCategory graffittiCategory,
        final GraffittiSubcategory graffittiSubcategory,
        final Geolocation geolocation,
        final Neighborhood neighborhood
    ) {
        String msg = "Looking for";

        if (graffittiSubcategory != null) {
            msg = msg + " " + graffittiSubcategory.name.toLowerCase();
        }

        msg = msg + " " + graffittiCategory.namePlural.toLowerCase();

        if (geolocation != null) {
            msg = msg + " near the location you specified";
        } else if (neighborhood != null) {
            msg = msg + " at " + neighborhood.name;
        }

        return msg;
    }
}
