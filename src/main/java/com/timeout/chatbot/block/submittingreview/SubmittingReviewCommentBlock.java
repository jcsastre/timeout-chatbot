package com.timeout.chatbot.block.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmittingReviewCommentBlock {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public SubmittingReviewCommentBlock(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            userId,
            "Please write a review. If you don't want to include a review just type 'no review'",
            QuickReply.newListBuilder()
                .addTextQuickReply(
                    "No review",
                    new JSONObject()
                        .put("type", PayloadType.utterance)
                        .put("utterance", "No review")
                        .toString()
                ).toList()
            .build()
        );
    }
}
