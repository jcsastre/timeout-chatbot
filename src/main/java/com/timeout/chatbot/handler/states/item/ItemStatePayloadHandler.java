package com.timeout.chatbot.handler.states.item;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.PhotosBlock;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.block.state.booking.BlockBookingBeginDeveloperNote;
import com.timeout.chatbot.block.state.booking.BlockBookingPeopleCount;
import com.timeout.chatbot.block.state.submittingreview.BlockSubmittingReviewRate;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.handler.intent.IntentBackHandler;
import com.timeout.chatbot.handler.intent.IntentPhotosHandler;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.bag.SessionStateSubmittingReviewBag;
import com.timeout.chatbot.session.state.BookingState;
import com.timeout.chatbot.session.state.SessionState;
import com.timeout.chatbot.session.state.SubmittingReviewState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ItemStatePayloadHandler {

    private final MessengerSendClient messengerSendClient;
    private final BlockBookingPeopleCount blockBookingPeopleCount;
    private final BlockBookingBeginDeveloperNote blockBookingBeginDeveloperNote;
    private final BlockSubmittingReviewRate blockSubmittingReviewRate;
    private final IntentBackHandler backHandler;
    private final PhotosBlock photosBlock;
    private final IntentPhotosHandler photosHandler;
    private final BlockError blockError;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public ItemStatePayloadHandler(
        MessengerSendClient messengerSendClient,
        BlockBookingPeopleCount blockBookingPeopleCount,
        BlockBookingBeginDeveloperNote blockBookingBeginDeveloperNote,
        BlockSubmittingReviewRate blockSubmittingReviewRate,
        IntentBackHandler backHandler,
        PhotosBlock photosBlock, IntentPhotosHandler photosHandler, BlockError blockError,
        QuickReplyBuilderHelper quickReplyBuilderHelper) {
        this.messengerSendClient = messengerSendClient;
        this.blockBookingPeopleCount = blockBookingPeopleCount;
        this.blockBookingBeginDeveloperNote = blockBookingBeginDeveloperNote;
        this.blockSubmittingReviewRate = blockSubmittingReviewRate;
        this.backHandler = backHandler;
        this.photosBlock = photosBlock;
        this.photosHandler = photosHandler;
        this.blockError = blockError;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case item_Book:
                handleBook(session);
                break;

            case item_Back:
                backHandler.handle(session);
                break;

            case item_Photos:
                photosHandler.handle(session);
                break;

            case item_SubmitPhoto:
                messengerSendClient.sendTextMessage(
                    session.getUser().getMessengerId(),
                    "Please attach one or more photos"
                );
                break;

            case item_SubmitReview:
                handleSumitReview(session);
                break;

            default:
                blockError.send(session.getUser());
                break;
        }
    }

    public void handlePhotos(Session session) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE) {
            photosBlock.send(
                session.getUser().getMessengerId(),
                itemBag.getVenue()
            );
        } else {
            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, 'Photos' feature is not implemented yet",
                quickReplyBuilderHelper.buildForSeeVenueItem(itemBag.getVenue())
            );
        }

    }

    public void handleBook(Session session) throws MessengerApiException, MessengerIOException {
        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE && session.getSessionStateSearchingBag().getCategory() == Category.RESTAURANTS) {

            blockBookingBeginDeveloperNote.send(session.getUser().getMessengerId());

            session.setSessionState(SessionState.BOOKING);
            final SessionStateBookingBag bookingBag = session.getSessionStateBookingBag();
            bookingBag.setBookingState(BookingState.PEOPLE_COUNT);
            blockBookingPeopleCount.send(session.getUser().getMessengerId());

        } else {

            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, 'Book' feature is not implemented yet",
                quickReplyBuilderHelper.buildForSeeVenueItem(itemBag.getVenue())
            );
        }
    }

    private void handleSumitReview(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionState sessionState = session.getSessionState();
        if (sessionState == SessionState.ITEM) {
            session.setSessionState(SessionState.SUBMITTING_REVIEW);
            final SessionStateSubmittingReviewBag bag = session.getSessionStateSubmittingReviewBag();
            bag.setSubmittingReviewState(SubmittingReviewState.RATING);
            bag.setRate(null);
            bag.setComment(null);
            blockSubmittingReviewRate.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }
}
