package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.SessionStateLookingBag;
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
        final SessionStateLookingBag bag = session.getSessionStateLookingBag();

        String itemPluralName = "Restaurants";
        if (bag.getWhat() == What.BAR) {
            itemPluralName = "Bars & Pubs";
        }

        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            String.format(
                "There are %s %s remaining",
                bag.getReaminingItems(), itemPluralName
            ),
            buildQuickReplies(
                bag
//                bag.getReaminingItems(),
//                isWhereSet,
//                isCategorySet,
//                categorySingularName
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        SessionStateLookingBag bag
//        Integer remainingItems,
//        Boolean isWhereSet,
//        Boolean isCategorySet,
//        String categorySingularName
    ) {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        if (bag.getReaminingItems() > 0) {
            listBuilder.addTextQuickReply(
                "See more",
                new JSONObject()
                    .put("type", PayloadType.see_more)
                    .toString()
            ).toList();
        }

        listBuilder.addLocationQuickReply().toList();

        listBuilder.addTextQuickReply(
            bag.getGraffittiWhere() != null ? "Change neighborhood" : "Set neighborhood",
            new JSONObject()
                .put("type", PayloadType.set_geolocation)
                .toString()
        ).toList();

        String categorySingularName = "Cuisine";
        if (bag.getWhat() == What.BAR) {
            categorySingularName = "Style";
        }

        listBuilder.addTextQuickReply(
            bag.getGraffittiWhatNode().getChildren() != null ?
                "Change " + categorySingularName : "Set " + categorySingularName,
            new JSONObject()
                .put("type", PayloadType.set_graffitti_category)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
