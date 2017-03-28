package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.state.booking.*;
import com.timeout.chatbot.action.SeeItemAction;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStateHandler {

    private final BlockBookingDate blockBookingDate;
    private final BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation;
    private final BlockBookingAskLastname blockBookingAskLastname;
    private final BlockBookingAskEmail blockBookingAskEmail;
    private final BlockBookingAskPhone blockBookingAskPhone;
    private final BlockConfirmationPersonalDetails blockConfirmationPersonalDetails;
    private final BlockBookingSubmitted blockBookingSubmitted;
    private final BlockBookingEndDeveloperNote blockBookingEndDeveloperNote;
    private final BlockBookingCancelled blockBookingCancelled;
    private final SeeItemAction seeItemAction;

    @Autowired
    public BookingStateHandler(
        BlockBookingDate blockBookingDate,
        BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockBookingAskEmail blockBookingAskEmail,
        BlockBookingAskPhone blockBookingAskPhone,
        BlockConfirmationPersonalDetails blockConfirmationPersonalDetails,
        BlockBookingSubmitted blockBookingSubmitted, BlockBookingEndDeveloperNote blockBookingEndDeveloperNote, BlockBookingCancelled blockBookingCancelled, SeeItemAction seeItemAction) {
        this.blockBookingDate = blockBookingDate;
        this.blockBookingFbLastNameConfirmation = blockBookingFbLastNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockBookingAskEmail = blockBookingAskEmail;
        this.blockBookingAskPhone = blockBookingAskPhone;
        this.blockConfirmationPersonalDetails = blockConfirmationPersonalDetails;
        this.blockBookingSubmitted = blockBookingSubmitted;
        this.blockBookingEndDeveloperNote = blockBookingEndDeveloperNote;
        this.blockBookingCancelled = blockBookingCancelled;
        this.seeItemAction = seeItemAction;
    }

    void setPeopleCountAndContinue(
        Session session,
        Integer peopleCount
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.bagBooking;
        bag.setPeopleCount(peopleCount);
        bag.setBookingState(BookingState.DATE);
        blockBookingDate.send(session.user.messengerId);
    }

    void setFirstNameAndContinue(
        Session session,
        String firstName
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.bagBooking;
        bag.setFirstName(firstName);
        bag.setBookingState(BookingState.LAST_NAME);

        final String lastName = session.fbUserProfile.getLastName();
        if (lastName != null) {
            blockBookingFbLastNameConfirmation.send(session);
        } else {
            blockBookingAskLastname.send(session.user.messengerId);
        }
    }

    void setLastNameAndContinue(
        Session session,
        String lastName
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.bagBooking;
        bag.setLastName(lastName);
        bag.setBookingState(BookingState.EMAIL);
        blockBookingAskEmail.send(session.user.messengerId);
    }

    void setEmailAndContinue(
        Session session,
        String email
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.bagBooking;
        bag.setEmail(email);
        bag.setBookingState(BookingState.PHONE);
        blockBookingAskPhone.send(session.user.messengerId);
    }

    void setPhoneAndContinue(
        Session session,
        String phone
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.bagBooking;
        bag.setPhone(phone);
        bag.setBookingState(BookingState.CONFIRMATION_PERSONAL_DETAILS);
        blockConfirmationPersonalDetails.send(
            session.user.messengerId,
            bag.getFirstName(),
            bag.getLastName(),
            bag.getEmail(),
            bag.getPhone()
        );
    }

    void confirmBooking(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        blockBookingSubmitted.send(session);
        blockBookingEndDeveloperNote.send(session.user.messengerId);

        session.state = SessionState.ITEM;
        seeItemAction.perform(session);
    }

    void canceldBooking(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        blockBookingCancelled.send(session.user.messengerId);
    }
}
