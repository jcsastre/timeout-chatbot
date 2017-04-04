package com.timeout.chatbot.handler.states.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewAskConfirmation;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewCancelled;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewComment;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewSubmitted;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.action.SeeItemAction;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubmittingReviewStatePayloadHandler {

    private final BlockSubmittingReviewComment blockSubmittingReviewComment;
    private final BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation;
    private final BlockSubmittingReviewSubmitted blockSubmittingReviewSubmitted;
    private final BlockSubmittingReviewCancelled blockSubmittingReviewCancelled;
    private final BlockError blockError;
    private final SeeItemAction seeItemAction;

    @Autowired
    public SubmittingReviewStatePayloadHandler(
        BlockSubmittingReviewComment blockSubmittingReviewComment,
        BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation,
        BlockSubmittingReviewSubmitted blockSubmittingReviewSubmitted,
        BlockSubmittingReviewCancelled blockSubmittingReviewCancelled,
        BlockError blockError,
        SeeItemAction seeItemAction
    ) {
        this.blockSubmittingReviewComment = blockSubmittingReviewComment;
        this.blockSubmittingReviewAskConfirmation = blockSubmittingReviewAskConfirmation;
        this.blockSubmittingReviewSubmitted = blockSubmittingReviewSubmitted;
        this.blockSubmittingReviewCancelled = blockSubmittingReviewCancelled;
        this.blockError = blockError;
        this.seeItemAction = seeItemAction;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case submitting_review_rate:
                handleSumittingReviewRate(session, payload);
                break;

            case submitting_review_no_comment:
                handleSumittingReviewNoComment(session);
                break;

            case submitting_review_confirmation_yes:
                handleSubmittingReviewConfirmation(session, Boolean.TRUE);
                //TODO
                break;

            case submitting_review_confirmation_no:
                handleSubmittingReviewConfirmation(session, Boolean.FALSE);
                //TODO
                break;

            default:
                blockError.send(session.user.messengerId);
                break;
        }
    }

    private void handleSumittingReviewRate(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.state;
        if (sessionState == SessionState.SUBMITTING_REVIEW) {

            final SessionStateSubmittingReviewBag bag = session.bagSubmitting;
            if (bag.state == SubmittingReviewState.RATING) {

                bag.rate = payload.getInt("rate");
                bag. state = SubmittingReviewState.WRITING_COMMENT;
                blockSubmittingReviewComment.send(session.user.messengerId);
            } else {
                blockError.send(session.user.messengerId);
            }
        } else {
            blockError.send(session.user.messengerId);
        }
    }

    private void handleSumittingReviewNoComment(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.state;
        if (sessionState == SessionState.SUBMITTING_REVIEW) {

            final SessionStateSubmittingReviewBag bag = session.bagSubmitting;
            if (bag.state == SubmittingReviewState.WRITING_COMMENT) {

                bag.state = SubmittingReviewState.ASKING_FOR_CONFIRMATION;
                blockSubmittingReviewAskConfirmation.send(session);
            } else {
                blockError.send(session.user.messengerId);
            }
        } else {
            blockError.send(session.user.messengerId);
        }
    }

    private void handleSubmittingReviewConfirmation(
        Session session,
        Boolean isYes
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionState sessionState = session.state;
        if (sessionState == SessionState.SUBMITTING_REVIEW) {

            final SessionStateSubmittingReviewBag bag = session.bagSubmitting;
            if (bag.state == SubmittingReviewState.ASKING_FOR_CONFIRMATION) {

                if (isYes) {
                    blockSubmittingReviewSubmitted.send(session.user.messengerId);
                } else {
                    blockSubmittingReviewCancelled.send(session.user.messengerId);
                }

                session.state = SessionState.ITEM;
                seeItemAction.perform(session);
            } else {
                blockError.send(session.user.messengerId);
            }
        } else {
            blockError.send(session.user.messengerId);
        }
    }
}
