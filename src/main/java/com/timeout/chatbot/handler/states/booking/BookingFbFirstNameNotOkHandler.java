package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingAskFirstname;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.stereotype.Component;

@Component
public class BookingFbFirstNameNotOkHandler {

    private final BlockBookingAskFirstname blockBookingAskFirstname;
    private final BlockError blockError;

    public BookingFbFirstNameNotOkHandler(
        BlockBookingAskFirstname blockBookingAskFirstname,
        BlockError blockError
    ) {
        this.blockBookingAskFirstname = blockBookingAskFirstname;
        this.blockError = blockError;
    }

    public void handle(
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
}
