package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.*;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.handler.intent.IntentSeeItem;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class BookingStatePayloadHandler {

    private final BookingStateHandler bookingStateHandler;
    private final BlockBookingTime blockBookingTime;
    private final BlockConfirmationBookingDetails blockConfirmationBookingDetails;
    private final BlockBookingAskFirstname blockBookingAskFirstname;
    private final BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation;
    private final BlockBookingAskLastname blockBookingAskLastname;
    private final BlockError blockError;
    private final IntentSeeItem intentSeeItem;

    @Autowired
    public BookingStatePayloadHandler(
        BookingStateHandler bookingStateHandler,
        BlockBookingTime blockBookingTime,
        BlockConfirmationBookingDetails blockConfirmationBookingDetails,
        BlockBookingAskFirstname blockBookingAskFirstname,
        BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockError blockError,
        IntentSeeItem intentSeeItem) {
        this.bookingStateHandler = bookingStateHandler;
        this.blockBookingTime = blockBookingTime;
        this.blockConfirmationBookingDetails = blockConfirmationBookingDetails;
        this.blockBookingAskFirstname = blockBookingAskFirstname;
        this.blockBookingFbFirstNameConfirmation = blockBookingFbFirstNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockError = blockError;
        this.intentSeeItem = intentSeeItem;
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

            case booking_date:
                handleDate(session, payload);
                break;

            case booking_time:
                handleTime(session, payload);
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
                blockError.send(session.getUser());
                break;
        }
    }

    private void handlePeopleCount(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.PEOPLE_COUNT) {

            bookingStateHandler.setPeopleCountAndContinue(
                session,
                payload.getInt("count")
            );
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleDate(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.DATE) {

            bag.setLocalDate(LocalDate.parse(payload.getString("date")));
            bag.setBookingState(BookingState.TIME);

            blockBookingTime.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleTime(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.TIME) {

            bag.setLocalTime(LocalTime.of(new Integer(payload.getString("time")), 0));

            bag.setBookingState(BookingState.CONFIRMATION_BOOKING_DETAILS);
            blockConfirmationBookingDetails.send(
                session.getUser().getMessengerId(),
                bag.getPeopleCount(),
                bag.getLocalDate(),
                bag.getLocalTime()
            );
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleInfoOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {
        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {

            bag.setBookingState(BookingState.FIRST_NAME);

            final User user = session.getUser();
            final String firstName = session.getFbUserProfile().getFirstName();
            if (firstName != null) {
                blockBookingFbFirstNameConfirmation.send(session);
            } else {
                blockBookingAskFirstname.send(user.getMessengerId());
            }
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleInfoNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {
            //TODO
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleFirstNameFbOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.FIRST_NAME) {

            bookingStateHandler.setFirstNameAndContinue(
                session,
                session.getFbUserProfile().getFirstName()
            );
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleFirstNameFbNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.FIRST_NAME) {
            blockBookingAskFirstname.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleLastNameFbOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.LAST_NAME) {

            bookingStateHandler.setLastNameAndContinue(
                session,
                session.getFbUserProfile().getLastName()
            );
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleLastNameFbNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.LAST_NAME) {
            blockBookingAskLastname.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleBookingPersonalInfoOk(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_PERSONAL_DETAILS) {

            bookingStateHandler.confirmBooking(session);
        } else {
            blockError.send(session.getUser());
        }
    }

    private void handleBookingPersonalInfoNotOk(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_PERSONAL_DETAILS) {

            bookingStateHandler.canceldBooking(session);
        } else {
            blockError.send(session.getUser());
        }
    }

}
