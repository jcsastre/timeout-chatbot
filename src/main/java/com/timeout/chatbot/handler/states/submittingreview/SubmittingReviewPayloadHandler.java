package com.timeout.chatbot.handler.states.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderForCurrentSessionState;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubmittingReviewPayloadHandler {

    private final BlockService blockService;
    private final MessengerSendClient messengerSendClient;
    private final QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState;

    @Autowired
    public SubmittingReviewPayloadHandler(
        BlockService blockService,
        MessengerSendClient messengerSendClient,
        QuickReplyBuilderForCurrentSessionState quickReplyBuilderForCurrentSessionState
    ) {
        this.blockService = blockService;
        this.messengerSendClient = messengerSendClient;
        this.quickReplyBuilderForCurrentSessionState = quickReplyBuilderForCurrentSessionState;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case submitting_review_rate:
                handleSumittingReviewRate(session, payload);
                break;

            case submitting_review_no_comment:
                handleSumittingReviewNoComment(session, payload);
                //TODO
                break;

            case temporaly_disabled:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, my creator has temporarily disabled the 'Search suggestions' :(",
                    quickReplyBuilderForCurrentSessionState.build(session)
                );
                break;

            default:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Sorry, I don't understand you."
                );
                break;
        }
    }

    private void handleSumittingReviewRate(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.getSessionState();
        if (sessionState == SessionState.SUBMITTING_REVIEW) {

            final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();
            final SubmittingReviewState submittingReviewState = bag.getSubmittingReviewState();
            if (submittingReviewState == SubmittingReviewState.RATING) {

                bag.setRate(payload.getInt("rate"));
                bag.setSubmittingReviewState(SubmittingReviewState.WRITING_COMMENT);
                blockService.sendSubmittingReviewCommentBlock(session.getUser().getMessengerId());
            } else {
                blockService.sendErrorBlock(session.getUser());
            }
        } else {
            blockService.sendErrorBlock(session.getUser());
        }
    }

    private void handleSumittingReviewNoComment(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.getSessionState();
        if (sessionState == SessionState.SUBMITTING_REVIEW) {

            final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();
            final SubmittingReviewState submittingReviewState = bag.getSubmittingReviewState();
            if (submittingReviewState == SubmittingReviewState.WRITING_COMMENT) {

                bag.setSubmittingReviewState(SubmittingReviewState.ASKING_FOR_CONFIRMATION);
                //TODO: send block for confirmation
            } else {
                blockService.sendErrorBlock(session.getUser());
            }
        } else {
            blockService.sendErrorBlock(session.getUser());
        }
    }
}
