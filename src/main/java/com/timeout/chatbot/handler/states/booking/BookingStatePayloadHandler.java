package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.action.SeeItemAction;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.*;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStatePayloadHandler {

    private final BookingStateHandler bookingStateHandler;
    private final BlockBookingAskTime blockBookingAskTime;
    private final BlockConfirmationBookingDetails blockConfirmationBookingDetails;
    private final BlockBookingAskFirstname blockBookingAskFirstname;
    private final BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation;
    private final BlockBookingAskLastname blockBookingAskLastname;
    private final BlockError blockError;
    private final SeeItemAction seeItemAction;

    @Autowired
    public BookingStatePayloadHandler(
        BookingStateHandler bookingStateHandler,
        BlockBookingAskTime blockBookingAskTime,
        BlockConfirmationBookingDetails blockConfirmationBookingDetails,
        BlockBookingAskFirstname blockBookingAskFirstname,
        BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockError blockError,
        SeeItemAction seeItemAction) {
        this.bookingStateHandler = bookingStateHandler;
        this.blockBookingAskTime = blockBookingAskTime;
        this.blockConfirmationBookingDetails = blockConfirmationBookingDetails;
        this.blockBookingAskFirstname = blockBookingAskFirstname;
        this.blockBookingFbFirstNameConfirmation = blockBookingFbFirstNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockError = blockError;
        this.seeItemAction = seeItemAction;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case booking_people_count:
                handlePeopleCount(session, payload);
                break;

            case booking_info_ok:
                handleInfoOk(session);
                break;

            case booking_info_not_ok:
                handleInfoNotOk(session);
                break;

            case booking_first_name_fb_ok:
                handleFirstNameFbOk(session);
                break;

            case booking_first_name_fb_not_ok:
                handleFirstNameFbNotOk(session);
                break;

            case booking_last_name_fb_ok:
                handleLastNameFbOk(session);
                break;

            case booking_last_name_fb_not_ok:
                handleLastNameFbNotOk(session);
                break;

            case booking_personal_info_ok:
                handleBookingPersonalInfoOk(session);
                break;

            case booking_personal_info_not_ok:
                handleBookingPersonalInfoNotOk(session);
                break;

            default:
                blockError.send(session.user.messengerId);
                break;
        }
    }

    private void handlePeopleCount(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (session.bagBooking.state == BookingState.PEOPLE_COUNT) {

            bookingStateHandler.setPeopleCountAndContinue(
                session,
                payload.getInt("count")
            );
        } else {
            blockError.send(session.user.messengerId);
        }
    }

    private void handleInfoOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {
//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {
//
//            bag.setBookingState(BookingState.FIRST_NAME);
//
//            final User user = session.user;
//            final String firstName = session.fbUserProfile.getFirstName();
//            if (firstName != null) {
//                blockBookingFbFirstNameConfirmation.send(session);
//            } else {
//                blockBookingAskFirstname.send(user.messengerId);
//            }
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleInfoNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {
//            //TODO
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleFirstNameFbOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.FIRST_NAME) {
//
//            bookingStateHandler.setFirstNameAndContinue(
//                session,
//                session.fbUserProfile.getFirstName()
//            );
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleFirstNameFbNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.FIRST_NAME) {
//            blockBookingAskFirstname.send(session.user.messengerId);
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleLastNameFbOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.LAST_NAME) {
//
//            bookingStateHandler.setLastNameAndContinue(
//                session,
//                session.fbUserProfile.getLastName()
//            );
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleLastNameFbNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.LAST_NAME) {
//            blockBookingAskLastname.send(session.user.messengerId);
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleBookingPersonalInfoOk(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.CONFIRMATION_PERSONAL_DETAILS) {
//
//            bookingStateHandler.confirmBooking(session);
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

    private void handleBookingPersonalInfoNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

//        final SessionStateBookingBag bag = session.bagBooking;
//        final BookingState bookingState = bag.getBookingState();
//        if (bookingState == BookingState.CONFIRMATION_PERSONAL_DETAILS) {
//
//            bookingStateHandler.canceldBooking(session);
//        } else {
//            blockError.send(session.user.messengerId);
//        }
    }

}
