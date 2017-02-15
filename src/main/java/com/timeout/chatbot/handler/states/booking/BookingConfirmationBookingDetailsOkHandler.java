package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingAskFirstname;
import com.timeout.chatbot.block.state.booking.BlockBookingFbFirstNameConfirmation;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationBookingDetailsOkHandler {

    private final BlockBookingAskFirstname blockBookingAskFirstname;
    private final BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation;
    private final BlockError blockError;

    public BookingConfirmationBookingDetailsOkHandler(
        BlockBookingAskFirstname blockBookingAskFirstname,
        BlockBookingFbFirstNameConfirmation blockBookingFbFirstNameConfirmation, BlockError blockError
    ) {
        this.blockBookingAskFirstname = blockBookingAskFirstname;
        this.blockBookingFbFirstNameConfirmation = blockBookingFbFirstNameConfirmation;
        this.blockError = blockError;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {

            bag.setBookingState(BookingState.FIRST_NAME);

            final User user = session.getUser();
            final String firstName = user.getFbUserProfile().getFirstName();
            if (firstName != null) {
                blockBookingFbFirstNameConfirmation.send(user);
            } else {
                blockBookingAskFirstname.send(user.getMessengerId());
            }
        } else {
            blockError.send(session.getUser());
        }
    }
}
