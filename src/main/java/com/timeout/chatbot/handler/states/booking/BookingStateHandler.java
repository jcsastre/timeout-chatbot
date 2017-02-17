package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.state.booking.*;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.handler.intent.IntentSeeItem;
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
    private final IntentSeeItem intentSeeItem;

    @Autowired
    public BookingStateHandler(
        BlockBookingDate blockBookingDate,
        BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockBookingAskEmail blockBookingAskEmail,
        BlockBookingAskPhone blockBookingAskPhone,
        BlockConfirmationPersonalDetails blockConfirmationPersonalDetails,
        BlockBookingSubmitted blockBookingSubmitted, BlockBookingEndDeveloperNote blockBookingEndDeveloperNote, BlockBookingCancelled blockBookingCancelled, IntentSeeItem intentSeeItem) {
        this.blockBookingDate = blockBookingDate;
        this.blockBookingFbLastNameConfirmation = blockBookingFbLastNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockBookingAskEmail = blockBookingAskEmail;
        this.blockBookingAskPhone = blockBookingAskPhone;
        this.blockConfirmationPersonalDetails = blockConfirmationPersonalDetails;
        this.blockBookingSubmitted = blockBookingSubmitted;
        this.blockBookingEndDeveloperNote = blockBookingEndDeveloperNote;
        this.blockBookingCancelled = blockBookingCancelled;
        this.intentSeeItem = intentSeeItem;
    }

    void setPeopleCountAndContinue(
        Session session,
        Integer peopleCount
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        bag.setPeopleCount(peopleCount);
        bag.setBookingState(BookingState.DATE);
        blockBookingDate.send(session.getUser().getMessengerId());
    }

    void setFirstNameAndContinue(
        Session session,
        String firstName
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        bag.setFirstName(firstName);
        bag.setBookingState(BookingState.LAST_NAME);

        final User user = session.getUser();
        final String lastName = user.getFbUserProfile().getLastName();
        if (lastName != null) {
            blockBookingFbLastNameConfirmation.send(user);
        } else {
            blockBookingAskLastname.send(user.getMessengerId());
        }
    }

    void setLastNameAndContinue(
        Session session,
        String lastName
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        bag.setLastName(lastName);
        bag.setBookingState(BookingState.EMAIL);
        blockBookingAskEmail.send(session.getUser().getMessengerId());
    }

    void setEmailAndContinue(
        Session session,
        String email
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        bag.setEmail(email);
        bag.setBookingState(BookingState.PHONE);
        blockBookingAskPhone.send(session.getUser().getMessengerId());
    }

    void setPhoneAndContinue(
        Session session,
        String phone
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        bag.setPhone(phone);
        bag.setBookingState(BookingState.CONFIRMATION_PERSONAL_DETAILS);
        blockConfirmationPersonalDetails.send(
            session.getUser().getMessengerId(),
            bag.getFirstName(),
            bag.getLastName(),
            bag.getEmail(),
            bag.getPhone()
        );
    }

    void confirmBooking(
        Session session
    ) throws MessengerApiException, MessengerIOException, IOException, InterruptedException {

        blockBookingSubmitted.send(session.getUser().getMessengerId());
        blockBookingEndDeveloperNote.send(session.getUser().getMessengerId());

        session.setSessionState(SessionState.ITEM);
        intentSeeItem.handle(session);
    }

    void canceldBooking(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        blockBookingCancelled.send(session.getUser().getMessengerId());
    }
}
