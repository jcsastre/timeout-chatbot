package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.response.Item;
import com.timeout.chatbot.graffitti.domain.response.categorisation.Categorisation;
import com.timeout.chatbot.graffitti.domain.response.categorisation.CategorisationSecondary;
import org.json.JSONObject;

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
            listBuilder.addElement(restaurant.getName())
                .imageUrl(restaurant.getImage_url())
                .subtitle(buildShortDescription(restaurant))
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "Más información",
                            new JSONObject()
                                .put("type", "restaurant-more-info")
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .addPostbackButton(
                            "Ver fotos",
                            new JSONObject()
                                .put("type", "restaurant-more-info")
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .addPostbackButton(
                            "Reservar",
                            new JSONObject()
                                .put("type", "restaurant-more-info")
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

    private String buildShortDescription(Item restaurant) {
        StringBuilder sb = new StringBuilder();

        sb.append("Restaurants");

        final Categorisation categorisation = restaurant.getCategorisation();
        if (categorisation != null) {
            final CategorisationSecondary categorisationSecondary = categorisation.getCategorisationSecondary();
            if (categorisationSecondary != null) {
                final String name = categorisationSecondary.getName();
                if (name != null) {
                    sb.append(", " + name);
                }
            }
        }

        if (restaurant.getLocation() != null) {
            sb.append(". " + restaurant.getLocation());
        }

        if (sb.length() > 80) {
            sb = sb.delete(80, sb.length());
        }

        return sb.toString();
    }
}
