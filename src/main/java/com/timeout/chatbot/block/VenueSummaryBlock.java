package com.timeout.chatbot.block;

import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.images.GraffittiImagesResponse;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import com.timeout.chatbot.graffitti.uri.GraffittiEndpoints;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class VenueSummaryBlock {

    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiEndpoints graffittiEndpoints;

    @Autowired
    public VenueSummaryBlock(
        RestTemplate restTemplate, GraffittiService graffittiService,
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiEndpoints graffittiEndpoints
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiEndpoints = graffittiEndpoints;
    }

    public void send(
        String userId,
        String venueId
    ) {

        final GraffittiVenueResponse graffittiVenueResponse =
            restTemplate.getForObject(
                graffittiEndpoints.getVenues() + venueId,
                GraffittiVenueResponse.class
            );

        sendSummaryIfAvailable(userId, graffittiVenueResponse);

        sendImagesIfAvailable(userId, venueId, graffittiVenueResponse);

//        sendNowWhatAndQuickReplies(userId, graffittiVenueResponse);

//        final ButtonTemplate yes = ButtonTemplate.newBuilder(
//            "Do you want to see full info at Timeout website?",
//            Button.newListBuilder()
//                .addUrlButton(
//                    "Yes",
//                    graffittiVenueResponse.getBody().getToWebsite()
//                ).toList()
//                .addPostbackButton(
//                    "No",
//                    new JSONObject()
//                        .put("type", PayloadType.no_see_at_timeout)
//                        .toString()
//                ).toList()
//
//                .buildButtonsList()
//        ).buildButtonsList();

//        messengerSendClientWrapper.sendTemplate(userId, yes);
    }

    private void sendImagesIfAvailable(String userId, String restaurantId, GraffittiVenueResponse restaurant) {
        String urlImages = graffittiEndpoints.getVenues() + restaurantId + "/graffittiImages";
        final GraffittiImagesResponse graffittiImagesResponse = restTemplate.getForObject(urlImages, GraffittiImagesResponse.class);

        List<GraffittiImage> graffittiImages = graffittiImagesResponse.getGraffittiImages();
        if (graffittiImages != null) {
            if (graffittiImages.size() > 10) {
                graffittiImages = graffittiImages.subList(0, 10);
            }

            final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
            final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
            for (GraffittiImage graffittiImage : graffittiImages) {
                String title = graffittiImage.getAltText();
                if (title == null) {
                    title = graffittiImage.getTitle();
                    if (title == null) {
                        title = restaurant.getBody().getName();
                    }
                }

                listBuilder.addElement(title).imageUrl(graffittiImage.getUrl()).toList().done();
            }
            final GenericTemplate genericTemplate = genericTemplateBuilder.build();

            messengerSendClientWrapper.sendTemplate(userId, genericTemplate);
        }
    }

    private void sendSummaryIfAvailable(String userId, GraffittiVenueResponse restaurant) {
        final String summary = restaurant.getBody().getSummary();
        if (summary!=null && !summary.isEmpty()) {
            messengerSendClientWrapper.sendTextMessage(
                userId,
                summary
            );
        }
    }

//    private void sendNowWhatAndQuickReplies(String userId, GraffittiVenueResponse venue) {
//        // Now what?
//        // See at timeout
//        // See at map
//        // Back to restaurants
//
//        messengerSendClientWrapper.sendTextMessage(
//            userId,
//            "GraffittiFacetV4Where you want to do now?",
//            buildQuickReplies(venue)
//        );
//    }


//    private List<QuickReply> buildQuickReplies(
//        GraffittiVenueResponse venue
//    ) {
//
//        final QuickReply.ListBuilder builder = QuickReply.newListBuilder();
//
//        final GraffittiFacetV4Body body = venue.getBody();
//        if (body != null) {
//
//            final String toWebsite = body.getToWebsite();
//            if (toWebsite != null) {
//                builder.addTextQuickReply()
//                                    .addCallButton(
//                        "Call",
//                        "+34678750727"
//                    ).toList();
//            }
//        }
//
//        if (remainingItems > 0) {
//            builder.addTextQuickReply(
//                "See more",
//                new JSONObject()
//                    .put("type", PayloadType.searching_SeeMore)
//                    .toString()
//            ).toList();
//        }
//
//        builder.addTextQuickReply(
//            isGeolocationSet ? "Change location" : "Set location",
//            new JSONObject()
//                .put("type", PayloadType.set_geolocation)
//                .toString()
//        ).toList();
//
//        return builder.buildButtonsList();
//    }
}
