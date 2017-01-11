package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.session.SessionContextBag;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VenuesPageBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public VenuesPageBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String userId,
        SessionContextBag.Geolocation userGeolocation,
        List<PageItem> pageItems,
        String itemPluralName,
        Integer nextPageNumber,
        Integer remainingItems
    ) {
        sendHorizontalCarroussel(
            userId,
            pageItems
        );

        sendFeedbackAndQuickReplies(
            userId,
            itemPluralName,
            userGeolocation,
            nextPageNumber,
            remainingItems
        );
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<PageItem> pageItems
    ) {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (PageItem pageItem : pageItems) {
            final GenericTemplate.Element.Builder elementBuilder = listBuilder.addElement(pageItem.getName());

            if (pageItem.getImage_url() != null) {
                elementBuilder.imageUrl(pageItem.getImage_url());
            }

            elementBuilder.subtitle(buildSubtitle(pageItem));

            elementBuilder.buttons(
                Button.newListBuilder()
                    .addPostbackButton(
                        "More info",
                        new JSONObject()
                            .put("type", PayloadType.venues_get_a_summary)
                            .put("venue_id", pageItem.getId())
                            .toString()
                    ).toList()
                    .addCallButton(
                        "Call",
                        "+34678750727"
                    ).toList()
                    .addPostbackButton(
                        "Book",
                        new JSONObject()
                            .put("type", PayloadType.venue_book)
                            .put("restaurant_id", pageItem.getId())
                            .toString()
                    ).toList()
                .build()
            ).toList().done();
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }

    private void sendFeedbackAndQuickReplies(
        String recipientId,
        String itemPluralName,
        SessionContextBag.Geolocation sessionGeolocation,
        Integer nextPageNumber,
        Integer remainingItems
    ) {
        String msg = "There are no remaining " + itemPluralName;
        if (remainingItems != null) {
            msg = String.format(
                "There are %s %s remaining",
                remainingItems, itemPluralName, itemPluralName
            );
        }

//        if (tooMuchItems) {
//            msg = msg + " \uD83D\uDE31.";
//
//            if (!suggestionRestaurantsFineSearchRequired) {
//                msg = msg + " Maybe you can search " + itemPluralName + " by location or by cuisine.";
//            }
//        }

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "See more",
            new JSONObject()
                .put("type", PayloadType.venues_see_more)
                .put("next_page_number", nextPageNumber)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            sessionGeolocation == null ? "Set location" : "Change location",
            new JSONObject()
                .put("type", PayloadType.set_location)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Set cuisine",
            new JSONObject()
                .put("type", PayloadType.venues_set_secondary_category)
                .toString()
        ).toList();

        messengerSendClientWrapper.sendTextMessage(
            recipientId,
            msg,
            listBuilder.build()
        );
    }

    private String buildSubtitle(PageItem pageItem) {
        StringBuilder sb = new StringBuilder();

        sb.append(pageItem.getCategorisation().buildName());

        if (pageItem.getLocation() != null) {
            sb.append(" ");
            sb.append("\uD83D\uDCCC");
            sb.append(" ");
            sb.append(pageItem.getLocation());
        }

        if (sb.length() > 80) {
            sb = sb.delete(80, sb.length());
        }

        return sb.toString();
    }
}
