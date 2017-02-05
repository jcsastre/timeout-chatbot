package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.template.generic.element.GenericTemplateElementVenueHelper;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class VenuesPageBlock {

    private final MessengerSendClient messengerSendClient;
    private final GenericTemplateElementVenueHelper genericTemplateElementVenueHelper;

    @Autowired
    public VenuesPageBlock(
        MessengerSendClient messengerSendClient,
        GenericTemplateElementVenueHelper genericTemplateElementVenueHelper
    ) {
        this.messengerSendClient = messengerSendClient;
        this.genericTemplateElementVenueHelper = genericTemplateElementVenueHelper;
    }

    public void send(
        Session session,
        List<PageItem> pageItems,
        String itemPluralName
    ) throws MessengerApiException, MessengerIOException {

        Assert.notNull(session, "The session must not be null");
        Assert.notNull(pageItems, "The pageItems must not be null");
        Assert.isTrue(pageItems.size() >= 1, "The pageItems size must be greater than zero");
        Assert.isTrue(pageItems.size() <= 10, "The pageItems size must be lesser than 10");
        Assert.notNull(itemPluralName, "The itemPluralName must not be null");

        sendHorizontalCarroussel(
            session.getUser().getMessengerId(),
            pageItems
        );
    }

    private void sendHorizontalCarroussel(
        String recipientId,
        List<PageItem> pageItems
    ) throws MessengerApiException, MessengerIOException {
        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (PageItem pageItem : pageItems) {
            genericTemplateElementVenueHelper.addElement(listBuilder, pageItem);
        }

        final GenericTemplate genericTemplate = genericTemplateBuilder.build();
        messengerSendClient.sendTemplate(recipientId, genericTemplate);
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
}
