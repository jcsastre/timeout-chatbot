package com.timeout.chatbot.handler.states.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewAskConfirmation;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubmittingReviewStateTextHandler {

    private final IntentService intentService;
    private final MessengerSendClient msc;
    private final BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation;
    private final BlockError blockError;

    @Autowired
    public SubmittingReviewStateTextHandler(
        IntentService intentService,
        MessengerSendClient msc,
        BlockSubmittingReviewAskConfirmation blockSubmittingReviewAskConfirmation,
        BlockError blockError
    ) {
        this.intentService = intentService;
        this.msc = msc;
        this.blockSubmittingReviewAskConfirmation = blockSubmittingReviewAskConfirmation;
        this.blockError = blockError;
    }

    public void handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();
        final SubmittingReviewState submittingReviewState = bag.getSubmittingReviewState();

        if (submittingReviewState==SubmittingReviewState.WRITING_COMMENT) {
            if (!text.equalsIgnoreCase("No review")) {
                bag.setComment(text);
            }
            bag.setSubmittingReviewState(SubmittingReviewState.ASKING_FOR_CONFIRMATION);
            blockSubmittingReviewAskConfirmation.send(session);
        } else if (submittingReviewState==SubmittingReviewState.ASKING_FOR_CONFIRMATION) {
            if (text.equalsIgnoreCase("Yes")) {
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Thanks! Your review has been submitted"
                );
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "[END OF SUBMIT REVIEW DEMO: No review has been submitted.]"
                );

                session.setSessionState(SessionState.ITEM);
                intentService.handleSeeItem(session);
            } else if (text.equalsIgnoreCase("No")) {
                msc.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "No problem. Your review has been canceled"
                );
                session.setSessionState(SessionState.ITEM);
                intentService.handleSeeItem(session);
            } else {
                blockError.send(session.getUser().getMessengerId());
            }
        } else {
            blockError.send(session.getUser().getMessengerId());
        }
    }
}
