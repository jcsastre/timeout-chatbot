package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.stereotype.Component;

@Component
public class BookingFbLastNameOkHandler {

    private final BlockError blockError;

    public BookingFbLastNameOkHandler(
        BlockError blockError
    ) {
        this.blockError = blockError;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.LAST_NAME) {

            bag.setLastName(session.getUser().getFbUserProfile().getLastName());

            //TODO

        } else {
            blockError.send(session.getUser());
        }
    }
}
