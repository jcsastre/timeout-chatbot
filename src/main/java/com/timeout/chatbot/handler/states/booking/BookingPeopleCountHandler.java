package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.block.state.booking.BlockBookingDate;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class BookingPeopleCountHandler {

    private final BlockBookingDate blockBookingDate;
    private final BlockError blockError;

    public BookingPeopleCountHandler(
        BlockBookingDate blockBookingDate,
        BlockError blockError
    ) {
        this.blockError = blockError;
        this.blockBookingDate = blockBookingDate;
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
        if (bookingState == BookingState.PEOPLE_COUNT) {

            bag.setPeopleCount(payload.getInt("count"));
            bag.setBookingState(BookingState.DATE);

            blockBookingDate.send(session.getUser().getMessengerId());
        } else {
            blockError.send(session.getUser());
        }
    }
}
