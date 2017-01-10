package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.Bar;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.graffitti.domain.response.images.Image;
import com.timeout.chatbot.graffitti.domain.response.images.ImagesResponse;
import com.timeout.chatbot.graffitti.endpoints.GraffittiEndpoints;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class VenueSummaryBlock {

    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public VenueSummaryBlock(
        RestTemplate restTemplate, GraffittiService graffittiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(String userId, String venueId) {

        final Bar venue =
            restTemplate.getForObject(
                GraffittiEndpoints.VENUE.toString() + venueId,
                Bar.class
            );

        sendSummary(userId, venue);

        sendImages(userId, venueId, venue);

        final ButtonTemplate yes = ButtonTemplate.newBuilder(
            "Do you want to see full info at Timeout website?",
            Button.newListBuilder()
                .addUrlButton(
                    "Yes",
                    venue.getBody().getToWebsite()
                ).toList()
                .build()
        ).build();

        messengerSendClientWrapper.sendTemplate(userId, yes);
    }

    private void sendImages(String userId, String restaurantId, Bar restaurant) {
        String urlImages = GraffittiEndpoints.VENUE.toString() + restaurantId + "/images";
        final ImagesResponse imagesResponse = restTemplate.getForObject(urlImages, ImagesResponse.class);

        List<Image> images = imagesResponse.getImages();
        if (images.size() > 10) {
            images = images.subList(0, 10);
        }

        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (Image image : images) {
            String title = image.getAltText();
            if (title == null) {
                title = image.getTitle();
                if (title == null) {
                    title = restaurant.getBody().getName();
                }
            }

            listBuilder.addElement(title).imageUrl(image.getUrl()).toList().done();
        }
        final GenericTemplate genericTemplate = genericTemplateBuilder.build();

        messengerSendClientWrapper.sendTemplate(userId, genericTemplate);
    }

    private void sendSummary(String userId, Bar restaurant) {
        messengerSendClientWrapper.sendTextMessage(
            userId,
            restaurant.getBody().getSummary()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (CategoryPrimary primaryCategoryPrimary : graffittiService.getPrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategoryPrimary.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategoryPrimary.getName())
                    .toString()
//                new JSONObject()
//                    .put("type", "search-by-primary-category")
//                    .put("uid", primaryCategoryPrimary.getId())
//                    .toApiaiAction()
            ).toList();
        }

        return listBuilder.build();
    }
}
