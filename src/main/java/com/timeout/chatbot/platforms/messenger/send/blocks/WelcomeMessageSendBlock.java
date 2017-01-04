package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.messenger.User;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WelcomeMessageSendBlock {

    private final GraffittiService graffittiService;
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public WelcomeMessageSendBlock(
        GraffittiService graffittiService,
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.graffittiService = graffittiService;
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(User user) {
        StringBuilder sbMessage = new StringBuilder();
        if (user.getUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + user.getUserProfile().getFirstName() + "!");
        } else {
            sbMessage.append("Hi!");
        }
        sbMessage.append("\n\n");
        sbMessage.append("I'm Julio, I work as chatbot on Timeout London.");
        sbMessage.append("\n\n");
        sbMessage.append("I know every corner in London, just ask me.");
        sbMessage.append("\n\n");
        sbMessage.append("What are you looking for?");

        messengerSendClientWrapper.sendTextMessage(
            user.getUid(),
            sbMessage.toString(),
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
//                new JSONObject()
//                    .put("type", "search-by-primary-category")
//                    .put("uid", primaryCategoryPrimary.getId())
//                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
