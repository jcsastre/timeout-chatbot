package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.state.booking.*;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingStateHandler {

    private final BlockBookingDate blockBookingDate;
    private final BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation;
    private final BlockBookingAskLastname blockBookingAskLastname;
    private final BlockBookingAskEmail blockBookingAskEmail;
    private final BlockBookingAskPhone blockBookingAskPhone;
    private final BlockConfirmationPersonalDetails blockConfirmationPersonalDetails;

    @Autowired
    public BookingStateHandler(
        BlockBookingDate blockBookingDate,
        BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockBookingAskEmail blockBookingAskEmail,
        BlockBookingAskPhone blockBookingAskPhone,
        BlockConfirmationPersonalDetails blockConfirmationPersonalDetails
    ) {
        this.blockBookingDate = blockBookingDate;
        this.blockBookingFbLastNameConfirmation = blockBookingFbLastNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockBookingAskEmail = blockBookingAskEmail;
        this.blockBookingAskPhone = blockBookingAskPhone;
        this.blockConfirmationPersonalDetails = blockConfirmationPersonalDetails;
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
}
