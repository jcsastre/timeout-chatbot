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
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
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

    public void perform(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        String userMessengerId = session.user.messengerId;
        final SessionStateSearchingBag bag = session.bagSearching;

        UrlBuilder urlBuilder =
            searchUrlBuilder.build(
                bag.graffittiSubcategory != null ? bag.graffittiSubcategory.graffittiId : bag.graffittiCategory.graffittiId,
                GraffittiType.VENUE.toValue(),
                bag.pageNumber
            );

        if (bag.geolocation != null) {
            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.LATITUDE.getValue(),
                        bag.geolocation.latitude.toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.LONGITUDE.getValue(),
                        bag.geolocation.longitude.toString()
                    )
                    .addParameter(
                        GraffittiQueryParameterType.RADIUS.getValue(),
                        "1"
                    )
                    .addParameter(
                        GraffittiQueryParameterType.SORT.getValue(),
                        "distance"
                    );
        } else if (bag.neighborhood != null) {
            urlBuilder =
                urlBuilder
                    .addParameter(
                        GraffittiQueryParameterType.WHERE.getValue(),
                        bag.neighborhood.graffitiId
                    );
        }

        senderActionsHelper.typingOn(userMessengerId);
        messengerSendClient.sendTextMessage(
            userMessengerId,
            buildMessage(
                bag.graffittiCategory,
                bag.graffittiSubcategory,
                bag.geolocation,
                bag.neighborhood
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

            bag.reaminingItems = graffittiSearchResponse.getRemainingItems();

            blockService.getVenuesPageBlock().send(
                userMessengerId,
                graffittiSearchResponse.getPageItems(),
                bag.graffittiCategory.namePlural
            );

            senderActionsHelper.typingOn(userMessengerId);
            blockService.getVenuesRemainingBlock().send(
                userMessengerId,
                bag.graffittiCategory,
                bag.graffittiSubcategory,
                bag.reaminingItems,
                bag.neighborhood,
                bag.geolocation
            );
        } else {
            messengerSendClient.sendTextMessage(
                userMessengerId,
                "There are not available " + bag.graffittiCategory.namePlural + " for your request."
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
