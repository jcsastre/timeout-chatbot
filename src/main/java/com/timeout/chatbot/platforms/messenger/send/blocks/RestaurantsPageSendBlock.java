package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.graffitti.domain.response.PageItem;
import com.timeout.chatbot.graffitti.domain.response.categorisation.Categorisation;
import com.timeout.chatbot.graffitti.domain.response.categorisation.CategorisationSecondary;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantsPageSendBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public RestaurantsPageSendBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String recipientId,
        List<PageItem> restaurants,
        Integer totalItems

    ) {
        sendHorizontalCarroussel(recipientId, restaurants);
        sendOptions(recipientId, totalItems);
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<PageItem> restaurants
    ) {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (PageItem restaurant : restaurants) {
            listBuilder.addElement(restaurant.getName())
                .imageUrl(restaurant.getImage_url())
                .subtitle(buildSubtitle(restaurant))
                .buttons(
                    Button.newListBuilder()
                        .addPostbackButton(
                            "More info",
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

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }

    private void sendOptions(
        String recipientId,
        Integer totalItems
    ) {
        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addLocationQuickReply().toList();

        listBuilder.addTextQuickReply(
            "Narrow by cuisine",
            new JSONObject()
                .put("type", "restaurants_page_narrow_by_cuisine")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "See more",
            new JSONObject()
                .put("type", "restaurants_page_see_more")
                .toString()
        ).toList();

        messengerSendClientWrapper.sendTextMessage(
            recipientId,
            "There are " + totalItems + " restaurants. Maybe you should narrow your search.",
            listBuilder.build()
        );
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

        if (sb.length() > 80) {
            sb = sb.delete(80, sb.length());
        }

        return sb.toString();
    }
}
