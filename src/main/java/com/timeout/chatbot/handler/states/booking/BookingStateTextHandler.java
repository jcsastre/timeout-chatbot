package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.state.booking.BlockBookingAskEmail;
import com.timeout.chatbot.block.state.booking.BlockBookingAskPeopleCount;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.session.Session;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStateTextHandler {

    private final MessengerSendClient msc;
    private final BlockBookingAskPeopleCount blockBookingAskPeopleCount;
    private final BookingStateHandler bookingStateHandler;
    private final BlockBookingAskEmail blockBookingAskEmail;

    @Autowired
    public BookingStateTextHandler(
        MessengerSendClient msc,
        BlockBookingAskPeopleCount blockBookingAskPeopleCount,
        BookingStateHandler bookingStateHandler,
        BlockBookingAskEmail blockBookingAskEmail) {
        this.blockBookingAskPeopleCount = blockBookingAskPeopleCount;
        this.msc = msc;
        this.bookingStateHandler = bookingStateHandler;
        this.blockBookingAskEmail = blockBookingAskEmail;
    }

    public boolean handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

        switch (session.bagBooking.state) {

            case PEOPLE_COUNT:
                handePeopleCount(text, session);
                break;

            case DATE:
                handleDate(text, session);
                break;

            case TIME:
                handleTime(text, session);
                break;

            case CONFIRMATION_BOOKING_DETAILS:
                handleConfirmationBookingDetails(text, session);
                break;

            case FIRST_NAME:
                handleFirstName(text, session);
                break;

            case LAST_NAME:
                handleLastName(text, session);
                break;

            case EMAIL:
                handleEmail(text, session);
                break;

            case PHONE:
                handlePhone(text, session);
                break;
        }

        return false;
    }

    private void handePeopleCount(
        String text,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        try {

            final int peopleCount = Integer.parseInt(text);

            bookingStateHandler.setPeopleCountAndContinue(session, peopleCount);

        } catch (NumberFormatException e) {
            final String userMessengerId = session.user.messengerId;
            msc.sendTextMessage(
                userMessengerId,
                "Please, enter a number for the number of people"
            );
            blockBookingAskPeopleCount.send(userMessengerId);
        }
    }

    private void handleDate(
        String text,
        Session session
    ) {
        //TODO
    }

    private void handleTime(
        String text,
        Session session
    ) {
        //TODO
    }

    private void handleConfirmationBookingDetails(
        String text,
        Session session
    ) {
        //TODO
    }

    private void handleFirstName(
        String firstName,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        bookingStateHandler.setFirstNameAndContinue(
            session,
            firstName
        );
    }

    private void handleLastName(
        String lastName,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        bookingStateHandler.setLastNameAndContinue(
            session,
            lastName
        );
    }

    private void handleEmail(
        String email,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (EmailValidator.getInstance().isValid(email)) {
            bookingStateHandler.setEmailAndContinue(
                session,
                email
            );
        } else {
            blockBookingAskEmail.send(session.user.messengerId);
        }
    }

    private void handlePhone(
        String phone,
        Session session
    ) throws MessengerApiException, MessengerIOException {

        bookingStateHandler.setPhoneAndContinue(
            session,
            phone
        );
    }
}
