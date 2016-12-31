package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.messenger.Recipient;
import org.json.JSONObject;

import java.util.List;

public abstract class WelcomeMessage {

    public static void send(MessengerSendClient messengerSendClient, Recipient recipient) {
        StringBuffer sbMessage = new StringBuffer();
        if (recipient.getUserProfile().getFirstName() != null) {
            sbMessage.append("Hi " + recipient.getUserProfile().getFirstName() + "!");
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
                recipient.getUid(),
                sbMessage.toString(),
                buildQuickReplies()
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private static List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder =
            QuickReply.newListBuilder()
                .addTextQuickReply(
                    "Things to do",
                    new JSONObject().put("type", "things-to-to").toString()
                ).toList()
                .addTextQuickReply("" +
                    "Restaurants",
                    new JSONObject().put("type", "restaurants").toString()
                ).toList()
                .addTextQuickReply("Bars and pubs", "PAYLOAD").toList()
                .addTextQuickReply("Art", "PAYLOAD").toList()
                .addTextQuickReply("Theatre", "PAYLOAD").toList()
                .addTextQuickReply("Music", "PAYLOAD").toList()
                .addTextQuickReply("Nightlife", "PAYLOAD").toList()
                .addTextQuickReply("Film", "PAYLOAD").toList();

        return listBuilder.build();
    }
}
