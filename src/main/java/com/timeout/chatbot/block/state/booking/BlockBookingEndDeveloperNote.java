package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlockBookingEndDeveloperNote {
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockBookingEndDeveloperNote(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {
        messengerSendClient.sendTextMessage(
            userId,
            "[Developer note: No booking has been submitted.]"
        );
    }
}
