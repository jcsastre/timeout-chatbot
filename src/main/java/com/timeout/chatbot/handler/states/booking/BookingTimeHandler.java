package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockConfirmationBookingDetails;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class BookingTimeHandler {

    private final BlockConfirmationBookingDetails blockConfirmationBookingDetails;
    private final BlockError blockError;

    public BookingTimeHandler(
        BlockConfirmationBookingDetails blockConfirmationBookingDetails,
        BlockError blockError
    ) {
        this.blockConfirmationBookingDetails = blockConfirmationBookingDetails;
        this.blockError = blockError;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.TIME) {

            bag.setLocalTime(LocalTime.of(new Integer(payload.getString("time")), 0));

            bag.setBookingState(BookingState.CONFIRMATION_BOOKING_DETAILS);
            blockConfirmationBookingDetails.send(
                session.getUser().getMessengerId(),
                bag.getPeopleCount(),
                bag.getLocalDate(),
                bag.getLocalTime()
            );
        } else {
            blockError.send(session.getUser());
        }
    }
}
