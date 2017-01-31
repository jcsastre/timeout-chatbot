package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscoverBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;

    @Autowired
    public DiscoverBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiService graffittiService
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiService = graffittiService;
    }

    public void send(
        String userId
    ) {

        messengerSendClientWrapper.sendTextMessage(
            userId,
            "GraffittiFacetV4Where you want to discover?",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (GraffittiFacetV5Node primaryCategoryPrimary : graffittiService.getFacetsV5PrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategoryPrimary.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategoryPrimary.getName())
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
