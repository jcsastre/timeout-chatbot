package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingAskLastname;
import com.timeout.chatbot.block.state.booking.BlockBookingFbLastNameConfirmation;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.stereotype.Component;

@Component
public class BookingFbFirstNameOkHandler {

    private final BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation;
    private final BlockBookingAskLastname blockBookingAskLastname;

    private final BlockError blockError;

    public BookingFbFirstNameOkHandler(
        BlockBookingFbLastNameConfirmation blockBookingFbLastNameConfirmation,
        BlockBookingAskLastname blockBookingAskLastname,
        BlockError blockError
    ) {
        this.blockBookingFbLastNameConfirmation = blockBookingFbLastNameConfirmation;
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockError = blockError;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.FIRST_NAME) {

            bag.setFirstName(session.getUser().getFbUserProfile().getFirstName());

            bag.setBookingState(BookingState.LAST_NAME);

            final User user = session.getUser();
            final String lastName = user.getFbUserProfile().getLastName();
            if (lastName != null) {
                blockBookingFbLastNameConfirmation.send(user);
            } else {
                blockBookingAskLastname.send(user.getMessengerId());
            }
        } else {
            blockError.send(session.getUser());
        }
    }
}
