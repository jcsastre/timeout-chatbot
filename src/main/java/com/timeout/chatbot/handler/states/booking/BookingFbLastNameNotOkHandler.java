package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingAskLastname;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.stereotype.Component;

@Component
public class BookingFbLastNameNotOkHandler {

    private final BlockBookingAskLastname blockBookingAskLastname;
    private final BlockError blockError;

    public BookingFbLastNameNotOkHandler(
        BlockBookingAskLastname blockBookingAskLastname,
        BlockError blockError
    ) {
        this.blockBookingAskLastname = blockBookingAskLastname;
        this.blockError = blockError;
    }

    public void handle(
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
}
