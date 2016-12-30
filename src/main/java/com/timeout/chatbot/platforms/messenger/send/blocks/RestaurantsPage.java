package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.response.Item;
import com.timeout.chatbot.platforms.messenger.postback.PostbackPayloadType;
import org.json.JSONObject;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

public class RestaurantsPage {
    private final MessengerSendClient messengerSendClient;
    private final List<Item> restaurants;
    private final String recipientId;

    public RestaurantsPage(
        MessengerSendClient messengerSendClient,
        List<Item> restaurants,
        String recipientId
    ) {
        this.messengerSendClient = messengerSendClient;
        this.restaurants = restaurants;
        this.recipientId = recipientId;
    }

    public void send() {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (Item restaurant : restaurants) {
            String shortDescription = HtmlUtils.htmlUnescape(restaurant.getSummary());
            if (shortDescription.length() > 80) {
                shortDescription = shortDescription.substring(0, 80);
            }

            listBuilder.addElement(restaurant.getName())
                .imageUrl(
                    restaurant.getImage_url()
                )
                .subtitle(
                    shortDescription
                )
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "Más información",
                            new JSONObject()
                                .put("type", PostbackPayloadType.RESTAURANT_MORE_INFO)
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .addPostbackButton(
                            "Ver fotos",
                            new JSONObject()
                                .put("type", PostbackPayloadType.RESTAURANT_MORE_INFO)
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .addPostbackButton(
                            "Reservar",
                            new JSONObject()
                                .put("type", PostbackPayloadType.RESTAURANT_MORE_INFO)
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .build()
                )
                .toList().done();
        }
        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        try {
            messengerSendClient.sendTemplate(recipientId, genericTemplate);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }
}
