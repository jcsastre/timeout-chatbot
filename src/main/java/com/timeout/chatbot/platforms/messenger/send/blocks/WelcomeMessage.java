package com.timeout.chatbot.platforms.messenger.send.blocks;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.messenger.Recipient;

import java.util.List;

public class WelcomeMessage {

    private MessengerSendClient messengerSendClient;
    private Recipient recipient;

    public WelcomeMessage(MessengerSendClient messengerSendClient, Recipient recipient) {
        this.messengerSendClient = messengerSendClient;
        this.recipient = recipient;
    }

    public void send() {
        try {
            this.messengerSendClient.sendTextMessage(
                this.recipient.getUid(),
                "Hi!\n" +
                    "\n" +
                    "I'm Julio :), I work as chat bot in Timeout.\n" +
                    "\n" +
                    "What are you looking for?",
                buildQuickReplies()
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private List<QuickReply> buildQuickReplies() {
        final QuickReply.ListBuilder listBuilder =
            QuickReply.newListBuilder()
                .addTextQuickReply("Tickets", "PAYLOAD").toList()
                .addTextQuickReply("Tickets", "PAYLOAD").toList()
                .addTextQuickReply("Tickets", "PAYLOAD").toList();

        return listBuilder.build();
    }
}
