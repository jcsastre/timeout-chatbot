package com.timeout.chatbot.block.submittingreview;

import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmittingReviewConfirmationBlock {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public SubmittingReviewConfirmationBlock(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        Session session
    ) {
        //TODO
    }
}
