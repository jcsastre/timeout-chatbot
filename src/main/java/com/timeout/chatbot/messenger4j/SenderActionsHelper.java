package com.timeout.chatbot.messenger4j;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.SenderAction;
import org.springframework.stereotype.Component;

@Component
public class SenderActionsHelper {

    private final MessengerSendClient messengerSendClient;

    public SenderActionsHelper(MessengerSendClient messengerSendClient) {
        this.messengerSendClient = messengerSendClient;
    }

    public void typingOn(
        String messengerId
    ) {
        try {
            messengerSendClient.sendSenderAction(
                messengerId,
                SenderAction.TYPING_ON
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void typingOnAndWait(
        String messengerId,
        long milis
    ) {
        typingOn(messengerId);

        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void typingOff(
        String messengerId
    ) {
        try {
            messengerSendClient.sendSenderAction(
                messengerId,
                SenderAction.TYPING_OFF
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void markSeen(
        String messengerId
    ) {
        try {
            messengerSendClient.sendSenderAction(
                messengerId,
                SenderAction.MARK_SEEN
            );
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }
}
