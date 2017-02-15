package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingAskFirstname;
import com.timeout.chatbot.block.state.booking.BlockBookingFirstnameConfirmation;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationBookingDetailsOkHandler {

    private final BlockBookingAskFirstname blockBookingAskFirstname;
    private final BlockBookingFirstnameConfirmation blockBookingFirstnameConfirmation;
    private final BlockError blockError;

    public BookingConfirmationBookingDetailsOkHandler(
        BlockBookingAskFirstname blockBookingAskFirstname,
        BlockBookingFirstnameConfirmation blockBookingFirstnameConfirmation, BlockError blockError
    ) {
        this.blockBookingAskFirstname = blockBookingAskFirstname;
        this.blockBookingFirstnameConfirmation = blockBookingFirstnameConfirmation;
        this.blockError = blockError;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.CONFIRMATION_BOOKING_DETAILS) {

            bag.setBookingState(BookingState.FIRST_NAME);

            final User user = session.getUser();
            final String firstName = user.getFbUserProfile().getFirstName();
            if (firstName != null) {
                blockBookingFirstnameConfirmation.send(user.getMessengerId(), firstName);
            } else {
                blockBookingAskFirstname.send(user.getMessengerId(), firstName);
            }
        } else {
            blockError.send(session.getUser());
        }
    }
}
