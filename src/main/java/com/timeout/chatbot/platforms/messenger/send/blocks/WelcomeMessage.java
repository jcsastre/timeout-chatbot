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
                "¡Hola!\n" +
                    "\n" +
                    "Me llamo Julio, y soy el chat bot de Timeout Barcelona.\n" +
                    "\n" +
                    "Me conozco todos los rincones de esta bonita ciudad, sólo tienes que preguntarme.\n" +
                    "\n" +
                    "¿Qué andas buscando?",
                buildQuickReplies()
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private List<QuickReply> buildQuickReplies() {
        final QuickReply.ListBuilder listBuilder =
            QuickReply.newListBuilder()
                .addTextQuickReply("Qué hacer", "PAYLOAD").toList()
                .addTextQuickReply("Restaurantes", "PAYLOAD").toList()
                .addTextQuickReply("Bares y pubs", "PAYLOAD").toList()
                .addTextQuickReply("Arte", "PAYLOAD").toList()
                .addTextQuickReply("Música", "PAYLOAD").toList()
                .addTextQuickReply("Locales de noche", "PAYLOAD").toList();

        return listBuilder.build();
    }
}
