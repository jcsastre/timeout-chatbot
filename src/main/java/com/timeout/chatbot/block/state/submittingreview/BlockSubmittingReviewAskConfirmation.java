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
public class BlockSubmittingReviewAskConfirmation {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockSubmittingReviewAskConfirmation(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        final SessionStateSubmittingReviewBag bag = session.stateSubmittingReviewBag;

        String msg = buildMessage(bag);

        messengerSendClient.sendTextMessage(
            session.user.messengerId,
            msg,
            QuickReply.newListBuilder()
                .addTextQuickReply(
                    "Yes",
                    new JSONObject()
                        .put("type", PayloadType.submitting_review_confirmation_yes)
                        .toString()
                ).toList()
                .addTextQuickReply(
                    "No",
                    new JSONObject()
                        .put("type", PayloadType.submitting_review_confirmation_no)
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
