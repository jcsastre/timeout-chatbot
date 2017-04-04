package com.timeout.chatbot.handler.messenger.quickreply;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.action.BackFromItemAction;
import com.timeout.chatbot.block.BlockPhotos;
import com.timeout.chatbot.block.state.booking.BlockBookingProposal;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewRate;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QuickReplyMessageEventHandlerItem {

    private final BackFromItemAction backFromItemAction;
    private final BlockPhotos blockPhotos;
    private final MessengerSendClient msc;
    private final BlockSubmittingReviewRate blockSubmittingReviewRate;
    private final BlockBookingProposal blockBookingProposal;

    @Autowired
    public QuickReplyMessageEventHandlerItem(
        BackFromItemAction backFromItemAction,
        BlockPhotos blockPhotos,
        MessengerSendClient msc,
        BlockSubmittingReviewRate blockSubmittingReviewRate,
        BlockBookingProposal blockBookingProposal
    ) {
        this.backFromItemAction = backFromItemAction;
        this.blockPhotos = blockPhotos;
        this.msc = msc;
        this.blockSubmittingReviewRate = blockSubmittingReviewRate;
        this.blockBookingProposal = blockBookingProposal;
    }

    public void handle(
        JSONObject payload,
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final QuickreplyPayload payloadType = QuickreplyPayload.valueOf(payload.getString("type"));
        switch (payloadType) {

            case item_back:
                handleBack(session);
                break;

            case item_book:
                handleBook(session);
                break;

            case item_photos:
                handlePhotos(session);
                break;

            case item_submit_review:
                handleSubmitReview(session);
                break;

            case item_submit_photo:
                handleSubmitPhoto(session);
                break;
        }
    }

    private void handleBack(
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        if (session.state == SessionState.ITEM) {

            backFromItemAction.perform(session);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleBook(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.ITEM) {

            session.state = SessionState.BOOKING;
            session.bagBooking = new SessionStateBookingBag();
            blockBookingProposal.send(session, true);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handlePhotos(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.ITEM) {

            blockPhotos.send(
                session.user.messengerId,
                session.bagItem.venue
            );
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSubmitReview(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.ITEM) {

            session.state = SessionState.SUBMITTING_REVIEW;
            session.bagSubmitting = new SessionStateSubmittingReviewBag();
            blockSubmittingReviewRate.send(session.user.messengerId);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleSubmitPhoto(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (session.state == SessionState.ITEM) {

            msc.sendTextMessage(
                session.user.messengerId,
                "Please attach one or more photos"
            );
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }
}
