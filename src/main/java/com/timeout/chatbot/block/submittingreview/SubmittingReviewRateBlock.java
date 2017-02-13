package com.timeout.chatbot.block.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmittingReviewRateBlock {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public SubmittingReviewRateBlock(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            userId,
            "Please, give your rate",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (int i=1; i<=5; i++) {
            listBuilder.addTextQuickReply(
                Integer.toString(i),
                new JSONObject()
                    .put("type", PayloadType.submitting_review_rate)
                    .put("rate", Integer.toString(i))
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
