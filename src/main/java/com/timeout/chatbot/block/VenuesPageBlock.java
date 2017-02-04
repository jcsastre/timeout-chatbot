package com.timeout.chatbot.block;

import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementVenueHelper;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class VenuesPageBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final TimeoutConfiguration timeoutConfiguration;
    private final GenericTemplateElementVenueHelper genericTemplateElementVenueHelper;

    @Autowired
    public VenuesPageBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        TimeoutConfiguration timeoutConfiguration,
        GenericTemplateElementVenueHelper genericTemplateElementVenueHelper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.timeoutConfiguration = timeoutConfiguration;
        this.genericTemplateElementVenueHelper = genericTemplateElementVenueHelper;
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
//        final GenericTemplate genericTemplate = genericTemplateBuilder.buildButtonsList();
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
            genericTemplateElementVenueHelper.addElement(listBuilder, pageItem);
//            final GenericTemplate.Element.Builder elementBuilder = listBuilder.addElement(pageItem.getName());
//
//            if (pageItem.getImage_url() != null) {
//                elementBuilder.imageUrl(pageItem.getImage_url());
//            } else {
//                elementBuilder.imageUrl("https://s3-eu-west-1.amazonaws.com/maps.timeout.com/cache/thumb_144_100/default/default.jpg");
//            }
//
//            String subtitle = genericTemplateElementVenueHelper.buildSubtitleForGenericTemplateElement(pageItem);
//            if (!subtitle.isEmpty()) {
//                elementBuilder.subtitle(subtitle);
//            }
//
//            elementBuilder.buttons(genericTemplateElementVenueHelper.buildButtonsList(pageItem)).toList().done();
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
//                            .put("type", PayloadType.venues_more_info)
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
//                    .buildButtonsList()
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
//        elementBuilder.buttons(buttonListBuilder.buildButtonsList()).toList().done();
//    }
}
