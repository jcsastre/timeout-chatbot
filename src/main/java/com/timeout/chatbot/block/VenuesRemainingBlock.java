package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionContextBag;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class VenuesRemainingBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public VenuesRemainingBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        Session session,
        String itemPluralName
    ) {
        Assert.notNull(session, "The session must not be null");
        Assert.notNull(itemPluralName, "The itemPluralName must not be null");

        final User user = session.getUser();
        final SessionContextBag sessionContextBag = session.getSessionContextBag();
        final Integer remainingItems = sessionContextBag.getReaminingItems();
        final Integer nextPageNumber = sessionContextBag.getPageNumber();
        final SessionContextBag.Geolocation geolocation = sessionContextBag.getGeolocation();

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            String.format(
                "There are %s %s remaining",
                remainingItems, itemPluralName
            ),
            buildQuickReplies(
                remainingItems,
                nextPageNumber,
                geolocation
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Integer remainingItems,
        Integer nextPageNumber,
        SessionContextBag.Geolocation sessionGeolocation
    ) {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        if (remainingItems > 0) {
            listBuilder.addTextQuickReply(
                "See more",
                new JSONObject()
                    .put("type", PayloadType.venues_see_more)
                    .toString()
            ).toList();
        }

        listBuilder.addTextQuickReply(
            sessionGeolocation == null ? "Set location" : "Change location",
            new JSONObject()
                .put("type", PayloadType.set_location)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}