package com.timeout.chatbot.messenger4j.send;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.*;
import com.github.messenger4j.send.templates.Template;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessengerSendClientWrapper {

    private final MessengerSendClient messengerSendClient;

    public MessengerSendClientWrapper(MessengerSendClient messengerSendClient) {
        this.messengerSendClient = messengerSendClient;
    }

    public void sendTextMessage(String recipientId, String text) {
//        if (text.length() > 320) {
//            text = text.substring(0, 320);
//        }

        try {
            messengerSendClient.sendTextMessage(
                recipientId,
                text
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(String recipientId, String text, List<QuickReply> quickReplies) {
        try {
            messengerSendClient.sendTextMessage(
                recipientId,
                text,
                quickReplies
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void sendImageAttachment(String recipientId, String imageUrl) {
        try {
            messengerSendClient.sendImageAttachment(
                recipientId,
                imageUrl
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void sendTemplate(String recipientId, Template template) {
        try {
            messengerSendClient.sendTemplate(
                recipientId,
                template
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void  sendTemplate(
        Recipient recipient,
        NotificationType notificationType,
        Template template,
        List<QuickReply> quickReplies,
        String metadata
    ) {
        try {
            messengerSendClient.sendTemplate(
                recipient,
                notificationType,
                template,
                quickReplies,
                metadata
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void sendVideoAttachment(
        String recipientId,
        String videoUrl
    ) {
        System.out.println(videoUrl);
        try {
            messengerSendClient.sendVideoAttachment(
                recipientId,
                videoUrl
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }
}
