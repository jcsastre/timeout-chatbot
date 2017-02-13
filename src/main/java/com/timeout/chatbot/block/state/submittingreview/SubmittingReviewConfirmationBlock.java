package com.timeout.chatbot.block.state.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import org.json.JSONObject;
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
    ) throws MessengerApiException, MessengerIOException {
        final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();

        String msg = buildMessage(bag);

        messengerSendClient.sendTextMessage(
            session.getUser().getMessengerId(),
            msg,
            QuickReply.newListBuilder()
                .addTextQuickReply(
                    "Yes",
                    new JSONObject()
                        .put("type", PayloadType.utterance)
                        .put("utterance", "Yes")
                        .toString()
                ).toList()
                .addTextQuickReply(
                    "No",
                    new JSONObject()
                        .put("type", PayloadType.utterance)
                        .put("utterance", "No")
                        .toString()
                ).toList()
            .build()
        );
    }

    private String buildMessage(SessionStateSubmittingReviewBag bag) {
        String msg =
            "Your rating is: " + bag.getRate() + "\n" +
            "\n";
        if (bag.getComment() != null) {
            msg = msg +
                "Your comment is: " + bag.getComment() + "\n" +
                "\n";
        }
        msg = msg + "Is that correct?";
        return msg;
    }
}
