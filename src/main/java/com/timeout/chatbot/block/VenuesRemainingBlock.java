package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        String userMessengerId,
        Integer remainingItems,
        Boolean isGeolocationSet,
        String itemPluralName
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userMessengerId,
            String.format(
                "There are %s %s remaining",
                remainingItems, itemPluralName
            ),
            buildQuickReplies(
                remainingItems,
                isGeolocationSet
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Integer remainingItems,
        Boolean isGeolocationSet
    ) {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        if (remainingItems > 0) {
            listBuilder.addTextQuickReply(
                "See more",
                new JSONObject()
                    .put("type", PayloadType.see_more)
                    .toString()
            ).toList();
        }

        listBuilder.addTextQuickReply(
            isGeolocationSet ? "Change location" : "Set location",
            new JSONObject()
                .put("type", PayloadType.set_geolocation)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
