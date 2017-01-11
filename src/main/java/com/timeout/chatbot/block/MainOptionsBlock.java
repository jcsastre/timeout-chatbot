package com.timeout.chatbot.block;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainOptionsBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final GraffittiService graffittiService;

    @Autowired
    public MainOptionsBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        GraffittiService graffittiService
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.graffittiService = graffittiService;
    }

    public void send(
        User user
    ) {
        String msg = "What are you looking for?";

        messengerSendClientWrapper.sendTextMessage(
            user.getMessengerId(),
            msg,
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (CategoryPrimary primaryCategoryPrimary : graffittiService.getPrimaryCategories()) {
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