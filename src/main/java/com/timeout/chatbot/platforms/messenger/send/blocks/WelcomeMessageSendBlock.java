package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.messenger.User;
import com.timeout.chatbot.graffitti.domain.Category;
import com.timeout.chatbot.services.GraffittiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WelcomeMessageSendBlock {

    private final GraffittiService graffittiService;
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public WelcomeMessageSendBlock(
        GraffittiService graffittiService,
        MessengerSendClient messengerSendClient
    ) {
        this.graffittiService = graffittiService;
        this.messengerSendClient = messengerSendClient;
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

        try {
            messengerSendClient.sendTextMessage(
                user.getUid(),
                sbMessage.toString(),
                buildQuickReplies()
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (Category primaryCategory : graffittiService.getPrimaryCategories()) {
            listBuilder.addTextQuickReply(
                primaryCategory.getName(),
                new JSONObject()
                    .put("type", "utterance")
                    .put("utterance", primaryCategory.getName())
                    .toString()
//                new JSONObject()
//                    .put("type", "search-by-primary-category")
//                    .put("uid", primaryCategory.getUuid())
//                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
