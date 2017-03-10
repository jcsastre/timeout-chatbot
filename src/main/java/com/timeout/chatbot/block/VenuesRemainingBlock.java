package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Subcategory;
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
        final SessionStateSearchingBag bag = session.stateSearchingBag;

        messengerSendClientWrapper.sendTextMessage(
            session.user.messengerId,
            String.format(
                "There are %s %s more",
                bag.reaminingItems, bag.category.getNamePlural()
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

        if (bag.reaminingItems > 0) {
            listBuilder.addTextQuickReply(
                "See more",
                new JSONObject()
                    .put("type", PayloadType.searching_SeeMore)
                    .toString()
            ).toList();
        }

        boolean areaSet = false;
        if (bag.geolocation != null) {
            areaSet = true;
        } else {
            if (bag.neighborhood != null) {
                areaSet = true;
            }
        }

        listBuilder.addTextQuickReply(
            areaSet ? "Change area" : "Set area",
            new JSONObject()
                .put("type", PayloadType.searching_VenuesShowAreas)
                .put("pageNumber", 1)
                .toString()
        ).toList();

        final Category category = bag.category;
        final List<Subcategory> subcategories = category.getSubcategories();
        if (subcategories!= null && subcategories.size()>0) {
            final String subcategoryName = category.getSubcategoriesName().toLowerCase();
            listBuilder.addTextQuickReply(
                bag.subcategory == null ?
                    "Set " + subcategoryName : "Change " + subcategoryName,
                new JSONObject()
                    .put("type", PayloadType.searching_ShowSubcategories)
                    .put("pageNumber", 1)
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
