package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingTime;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingDateHandler {

    private final BlockBookingTime blockBookingTime;
    private final BlockError blockError;

    public BookingDateHandler(
        BlockBookingTime blockBookingTime,
        BlockError blockError
    ) {
        this.blockBookingTime = blockBookingTime;
        this.blockError = blockError;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        switch (session.getSessionState()) {

            case BOOKING:
                handleStateBooking(session, payload);
                break;

            default:
                blockError.send(session.getUser());
        }
    }

    private void handleStateBooking(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateBookingBag bag = session.getSessionStateBookingBag();
        final BookingState bookingState = bag.getBookingState();
        if (bookingState == BookingState.DATE) {

            bag.setLocalDate(LocalDate.parse(payload.getString("date")));
            bag.setBookingState(BookingState.TIME);

            blockBookingTime.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }
}
