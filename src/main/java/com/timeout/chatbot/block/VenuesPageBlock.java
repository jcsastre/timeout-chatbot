package com.timeout.chatbot.block;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.graffitti.domain.response.search.page.PageItem;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        Session session,
        List<PageItem> pageItems,
        String itemPluralName
    ) {
        Assert.notNull(session, "The session must not be null");
        Assert.notNull(pageItems, "The pageItems must not be null");
        Assert.isTrue(pageItems.size() >= 1, "The pageItems size must be greater than zero");
        Assert.isTrue(pageItems.size() <= 10, "The pageItems size must be lesser than 10");
        Assert.notNull(itemPluralName, "The itemPluralName must not be null");

        sendHorizontalCarroussel(
            session.getUser().getMessengerId(),
            pageItems
        );

//        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
//        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
//
//        addVenueItems(
//            listBuilder,
//            pageItems
//        );
//
//        addLastItem(
//            listBuilder,
//            remainingItems,
//            nextPageNumber,
//            itemPluralName,
//            geolocation
//        );
//
//        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
//
//        messengerSendClientWrapper.sendTemplate(userId, genericTemplate);
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

            elementBuilder.subtitle(buildVenuePageItemSubtitle(pageItem));

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
                            .put("type", PayloadType.venues_book)
                            .put("restaurant_id", pageItem.getId())
                            .toString()
                    ).toList()
                    .build()
            ).toList().done();
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClientWrapper.sendTemplate(recipientId, genericTemplate);
    }

//    private void addVenueItems(
//        GenericTemplate.Element.ListBuilder listBuilder,
//        List<PageItem> pageItems
//    ) {
//        for (PageItem pageItem : pageItems) {
//            final GenericTemplate.Element.Builder elementBuilder = listBuilder.addElement(pageItem.getName());
//
//            if (pageItem.getImage_url() != null) {
//                elementBuilder.imageUrl(pageItem.getImage_url());
//            }
//
//            elementBuilder.subtitle(buildVenuePageItemSubtitle(pageItem));
//
//            elementBuilder.buttons(
//                Button.newListBuilder()
//                    .addPostbackButton(
//                        "More info",
//                        new JSONObject()
//                            .put("type", PayloadType.venues_get_a_summary)
//                            .put("venue_id", pageItem.getId())
//                            .toString()
//                    ).toList()
//                    .addCallButton(
//                        "Call",
//                        "+34678750727"
//                    ).toList()
//                    .addPostbackButton(
//                        "Book",
//                        new JSONObject()
//                            .put("type", PayloadType.venues_book)
//                            .put("restaurant_id", pageItem.getId())
//                            .toString()
//                    ).toList()
//                    .build()
//            ).toList().done();
//        }
//    }

//    private void addLastItem(
//        @NotNull GenericTemplate.Element.ListBuilder listBuilder,
//        @NotNull Integer remainingItems,
//        Integer nextPageNumber,
//        @NotNull String itemPluralName,
//        SessionContextBag.Geolocation geolocation
//    ) {
//
//        String title = null;
//        if (remainingItems > 0) {
//            title = String.format(
//                "There are %s %s remaining",
//                remainingItems, itemPluralName
//            );
//        } else {
//            title = "There are no remaining " + itemPluralName;
//        }
//
//        final GenericTemplate.Element.Builder elementBuilder = listBuilder.addElement(title);
//
////        elementBuilder.subtitle(buildVenuePageItemSubtitle(pageItem));
//
//        final Button.ListBuilder buttonListBuilder = Button.newListBuilder();
//
//        if (remainingItems > 0) {
//            buttonListBuilder.addPostbackButton(
//                "See more",
//                new JSONObject()
//                    .put("type", PayloadType.venues_see_more)
//                    .put("next_page_number", nextPageNumber)
//                    .toString()
//            ).toList();
//        }
//
//        buttonListBuilder.addPostbackButton(
//            geolocation == null ? "Set location" : "Change location",
//            new JSONObject()
//                .put("type", PayloadType.set_location)
//                .toString()
//        ).toList();
//
//        elementBuilder.buttons(buttonListBuilder.build()).toList().done();
//    }

    private String buildVenuePageItemSubtitle(PageItem pageItem) {
        StringBuilder sb = new StringBuilder();

        sb.append(pageItem.getGraffittiCategorisation().buildName());

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
