package com.timeout.chatbot.handler.states.submittingreview;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubmittingReviewStateTextHandler {

    private final IntentService intentService;
    private final MessengerSendClient msc;
    private final BlockService blockService;

    public SubmittingReviewStateTextHandler(
        IntentService intentService,
        MessengerSendClient msc,
        BlockService blockService
    ) {
        this.intentService = intentService;
        this.msc = msc;
        this.blockService = blockService;
    }

    public void handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

        switch (session.bagSubmitting.state) {

            case RATING:
                handleRating(text, session);
                break;

            case WRITING_COMMENT:
                handleWritingComment(text, session);
                break;

            case ASKING_FOR_CONFIRMATION:
                handleAskingForConfirmation(text, session);
                break;

            default:
                blockService.getError().send(session.user.messengerId);
        }
    }

    public void handleRating(
        String text,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (
            text.equalsIgnoreCase("1") ||
            text.equalsIgnoreCase("2") ||
            text.equalsIgnoreCase("3") ||
            text.equalsIgnoreCase("4") ||
            text.equalsIgnoreCase("5")
        ) {
            session.bagSubmitting.rate = Integer.valueOf(text);
            session.bagSubmitting.state = SubmittingReviewState.WRITING_COMMENT;

            blockService.getSubmittingReviewComment().send(session.user.messengerId);
        } else {
            //TODO: mensaje diciendo que no es una respuesta válida
            blockService.getSubmittingReviewRate().send(session.user.messengerId);
        }
    }

    public void handleWritingComment(
        String text,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        session.bagSubmitting.comment = text;
        session.bagSubmitting.state = SubmittingReviewState.ASKING_FOR_CONFIRMATION;

        blockService.getSubmittingReviewAskConfirmation().send(session);
    }

    public void handleAskingForConfirmation(
        String text,
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        blockService.getSubmittingReviewAskConfirmation().send(session);

//        if (text.equalsIgnoreCase("Yes")) {
//
//            msc.sendTextMessage(
//                session.user.messengerId,
//                "Thanks! Your review has been submitted"
//            );
//            msc.sendTextMessage(
//                session.user.messengerId,
//                "[END OF SUBMIT REVIEW DEMO: No review has been submitted.]"
//            );
//
//            session.state = SessionState.ITEM;
//            intentService.getIntentSeeItem().handle(session);
//
//            return true;
//
//        } else if (text.equalsIgnoreCase("No")) {
//
//            msc.sendTextMessage(
//                session.user.messengerId,
//                "No problem. Your review has been canceled"
//            );
//
//            session.state = SessionState.ITEM;
//            intentService.getIntentSeeItem().handle(session);
//
//            return true;
//        }
//
//        return false;
    }
}
