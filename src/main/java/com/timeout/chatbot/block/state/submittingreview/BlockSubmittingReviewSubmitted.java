package com.timeout.chatbot.block.state.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlockSubmittingReviewSubmitted {
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockSubmittingReviewSubmitted(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            userId,
            "Thanks! Your review has been submitted"
        );

        messengerSendClient.sendTextMessage(
            userId,
            "[Developer note: No review has been submitted]"
        );
    }
}
