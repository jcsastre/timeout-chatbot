package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSearchingBag;
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
    ) {
        final SessionStateSearchingBag bag = session.getSessionStateSearchingBag();

        String itemPluralName = "Restaurants";
        if (bag.getWhat() == What.BAR) {
            itemPluralName = "Bars & Pubs";
        }

        messengerSendClientWrapper.sendTextMessage(
            session.getUser().getMessengerId(),
            String.format(
                "There are %s %s more",
                bag.getReaminingItems(), itemPluralName
            ),
            buildQuickReplies(
                bag
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        SessionStateSearchingBag bag
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

        listBuilder.addTextQuickReply(
            "Area",
            new JSONObject()
                .put("type", PayloadType.venues_show_areas)
                .put("pageNumber", 1)
                .toString()
        ).toList();

//        String categorySingularName = "Cuisine";
//        if (bag.getWhat() == What.BAR) {
//            categorySingularName = "Style";
//        }
//
//        listBuilder.addTextQuickReply(
//            bag.getGraffittiWhatCategoryNode().getChildren() == null ?
//                "Change " + categorySingularName : "Set " + categorySingularName,
//            new JSONObject()
//                .put("type", PayloadType.show_subcategories)
//                .toString()
//        ).toList();

        return listBuilder.build();
    }
}
