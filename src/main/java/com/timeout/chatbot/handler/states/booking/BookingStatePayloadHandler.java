package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStatePayloadHandler {

    private final BlockError blockError;
    private final BookingPeopleCountHandler bookingPeopleCountHandler;
    private final BookingDateHandler bookingDateHandler;
    private final BookingTimeHandler bookingTimeHandler;
    private final BookingConfirmationBookingDetailsOkHandler bookingConfirmationBookingDetailsOkHandler;
    private final BookingConfirmationBookingDetailsNotOkHandler bookingConfirmationBookingDetailsNotOkHandler;
    private final BookingFbFirstNameOkHandler bookingFbFirstNameOkHandler;
    private final BookingFbFirstNameNotOkHandler bookingFbFirstNameNotOkHandler;
    private final BookingFbLastNameOkHandler bookingFbLastNameOkHandler;
    private final BookingFbLastNameNotOkHandler bookingFbLastNameNotOkHandler;

    @Autowired
    public BookingStatePayloadHandler(
        BlockError blockError,
        BookingPeopleCountHandler bookingPeopleCountHandler,
        BookingDateHandler bookingDateHandler,
        BookingTimeHandler bookingTimeHandler,
        BookingConfirmationBookingDetailsOkHandler bookingConfirmationBookingDetailsOkHandler,
        BookingConfirmationBookingDetailsNotOkHandler bookingConfirmationBookingDetailsNotOkHandler,
        BookingFbFirstNameOkHandler bookingFbFirstNameOkHandler, BookingFbFirstNameNotOkHandler bookingFbFirstNameNotOkHandler, BookingFbLastNameOkHandler bookingFbLastNameOkHandler, BookingFbLastNameNotOkHandler bookingFbLastNameNotOkHandler) {
        this.blockError = blockError;
        this.bookingPeopleCountHandler = bookingPeopleCountHandler;
        this.bookingDateHandler = bookingDateHandler;
        this.bookingTimeHandler = bookingTimeHandler;
        this.bookingConfirmationBookingDetailsOkHandler = bookingConfirmationBookingDetailsOkHandler;
        this.bookingConfirmationBookingDetailsNotOkHandler = bookingConfirmationBookingDetailsNotOkHandler;
        this.bookingFbFirstNameOkHandler = bookingFbFirstNameOkHandler;
        this.bookingFbFirstNameNotOkHandler = bookingFbFirstNameNotOkHandler;
        this.bookingFbLastNameOkHandler = bookingFbLastNameOkHandler;
        this.bookingFbLastNameNotOkHandler = bookingFbLastNameNotOkHandler;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case booking_people_count:
                bookingPeopleCountHandler.handle(session, payload);
                break;

            case booking_date:
                bookingDateHandler.handle(session, payload);
                break;

            case booking_time:
                bookingTimeHandler.handle(session, payload);
                break;

            case booking_info_ok:
                bookingConfirmationBookingDetailsOkHandler.handle(session);
                break;

            case booking_info_not_ok:
                bookingConfirmationBookingDetailsNotOkHandler.handle(session, payload);
                break;

            case booking_first_name_fb_ok:
                bookingFbFirstNameOkHandler.handle(session);
                break;

            case booking_first_name_fb_not_ok:
                bookingFbFirstNameNotOkHandler.handle(session);
                break;

            case booking_last_name_fb_ok:
                bookingFbLastNameOkHandler.handle(session);
                break;

            case booking_last_name_fb_not_ok:
                bookingFbLastNameNotOkHandler.handle(session);
                break;

            default:
                blockError.send(session.getUser());
                break;
        }
    }
}
