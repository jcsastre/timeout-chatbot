package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
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
        Session session
//        String userMessengerId,
//        Integer remainingItems,
//        Boolean isWhereSet,
//        String itemPluralName,
//        Boolean isCategorySet,
//        String categorySingularName
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userMessengerId,
            String.format(
                "There are %s %s remaining",
                remainingItems, itemPluralName
            ),
            buildQuickReplies(
                remainingItems,
                isWhereSet,
                isCategorySet,
                categorySingularName
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        Integer remainingItems,
        Boolean isWhereSet,
        Boolean isCategorySet,
        String categorySingularName
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

        listBuilder.addLocationQuickReply().toList();

        listBuilder.addTextQuickReply(
            isWhereSet ? "Change neighbourhood" : "Set neighbourhood",
            new JSONObject()
                .put("type", PayloadType.set_geolocation)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            isCategorySet ? "Change " + categorySingularName : "Set " + categorySingularName,
            new JSONObject()
                .put("type", PayloadType.set_geolocation)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
