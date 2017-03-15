package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
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
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Integer reaminingItems,
        Neighborhood neighborhood,
        Geolocation geolocation
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userMessengerId,
            String.format(
                "There are %s %s more",
                reaminingItems, graffittiCategory.getNamePlural()
            ),
            buildQuickReplies(
                graffittiCategory,
                graffittiSubcategory,
                reaminingItems,
                neighborhood,
                geolocation
            )
        );
    }

    private List<QuickReply> buildQuickReplies(
        GraffittiCategory graffittiCategory,
        GraffittiSubcategory graffittiSubcategory,
        Integer reaminingItems,
        Neighborhood neighborhood,
        Geolocation geolocation
    ) {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        if (reaminingItems > 0) {
            listBuilder.addTextQuickReply(
                "See more",
                new JSONObject()
                    .put("type", PayloadType.searching_SeeMore)
                    .toString()
            ).toList();
        }

        boolean areaSet = false;
        if (geolocation != null) {
            areaSet = true;
        } else {
            if (neighborhood != null) {
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

        final List<GraffittiSubcategory> graffittiSubcategories = graffittiCategory.getSubcategories();
        if (graffittiSubcategories!= null && graffittiSubcategories.size()>0) {
            final String subcategoryName = graffittiCategory.getSubcategoriesName().toLowerCase();
            listBuilder.addTextQuickReply(
                graffittiSubcategory == null ?
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
