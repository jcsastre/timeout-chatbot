package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffiti.endpoints.GraffittiEndpoints;
import com.timeout.chatbot.graffitti.domain.Category;
import com.timeout.chatbot.graffitti.domain.Restaurant;
import com.timeout.chatbot.graffitti.domain.response.images.Image;
import com.timeout.chatbot.graffitti.domain.response.images.ImagesResponse;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestaurantSummarySendBlock {

    private final RestTemplate restTemplate;
    private final GraffittiService graffittiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public RestaurantSummarySendBlock(
        RestTemplate restTemplate, GraffittiService graffittiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.restTemplate = restTemplate;
        this.graffittiService = graffittiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(String userId, String restaurantId) {

        String url = GraffittiEndpoints.VENUE.toString() + restaurantId;
        final Restaurant restaurant = restTemplate.getForObject(url, Restaurant.class);

        String urlImages = GraffittiEndpoints.VENUE.toString() + restaurantId + "/images";
        final ImagesResponse imagesResponse = restTemplate.getForObject(urlImages, ImagesResponse.class);

        messengerSendClientWrapper.sendTextMessage(
            userId,
            restaurant.getBody().getSummary()
        );

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

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (Category primaryCategory : graffittiService.getPrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategory.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategory.getName())
                    .toString()
//                new JSONObject()
//                    .put("type", "search-by-primary-category")
//                    .put("uid", primaryCategory.getUuid())
//                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
