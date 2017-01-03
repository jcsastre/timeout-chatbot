package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.response.PageItem;
import com.timeout.chatbot.graffitti.domain.response.categorisation.Categorisation;
import com.timeout.chatbot.graffitti.domain.response.categorisation.CategorisationSecondary;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;

import java.util.List;

public class RestaurantsPage {
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final List<PageItem> restaurants;
    private final String recipientId;

    public RestaurantsPage(
        MessengerSendClientWrapper messengerSendClientWrapper,
        List<PageItem> restaurants,
        String recipientId
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.restaurants = restaurants;
        this.recipientId = recipientId;
    }

    public void send() {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (PageItem restaurant : restaurants) {
            listBuilder.addElement(restaurant.getName())
                .imageUrl(restaurant.getImage_url())
                .subtitle(buildSubtitle(restaurant))
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "Get a summary",
                            new JSONObject()
                                .put("type", "restaurant_get_a_summary")
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
                        .addCallButton(
                            "Call",
                            "+34678750727"
                        ).toList()
                        .addPostbackButton(
                            "Book",
                            new JSONObject()
                                .put("type", "restaurant_book")
                                .put("restaurant_id", restaurant.getId())
                                .toString()
                        ).toList()
//                        .addUrlButton(
//                            "See at Timeout",
//                            restaurant.getToWebsite()
//                        ).toList()
                        .build()
                )
                .toList().done();
        }

        // Last item-summary
        listBuilder.addElement("292 restaurants more")
            .buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        "See more",
                        new JSONObject()
                            .put("type", "restaurant_get_a_summary")
                            .toString()
                    ).toList()
                    .addPostbackButton(
                        "Near me",
                        new JSONObject()
                            .put("type", "restaurant_get_a_summary")
                            .toString()
                    ).toList()
                    .addPostbackButton(
                        "By food",
                        new JSONObject()
                            .put("type", "restaurant_get_a_summary")
                            .toString()
                    ).toList()
                    .build()
            )
            .toList().done();

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }

    private String buildSubtitle(PageItem restaurant) {
        StringBuilder sb = new StringBuilder();

        boolean secondaryCategoryFound = false;
        final Categorisation categorisation = restaurant.getCategorisation();
        if (categorisation != null) {
            final CategorisationSecondary categorisationSecondary = categorisation.getCategorisationSecondary();
            if (categorisationSecondary != null) {
                final String categorisationSecondaryName = categorisationSecondary.getName();
                if (categorisationSecondaryName != null) {
                    sb.append(categorisationSecondaryName + " restaurant");
                    secondaryCategoryFound = true;
                }
            }
        }
        if (secondaryCategoryFound == false) {
            sb.append("Restaurant");
        }

        if (restaurant.getLocation() != null) {
            sb.append(" ");
            sb.append("\uD83D\uDCCC");
            sb.append(" ");
            sb.append(restaurant.getLocation());
        }

//        sb.append("\uD83D\uDEA9");
//        sb.append(restaurant.getSummary());

        if (sb.length() > 80) {
            sb = sb.delete(80, sb.length());
        }

        return sb.toString();
    }
}
