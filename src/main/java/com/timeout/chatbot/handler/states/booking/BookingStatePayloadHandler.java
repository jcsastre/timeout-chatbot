package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStatePayloadHandler {

    private final BlockService blockService;
    private final BookingPeopleCountHandler bookingPeopleCountHandler;
    private final BookingDateHandler bookingDateHandler;
    private final BookingTimeHandler bookingTimeHandler;

    @Autowired
    public BookingStatePayloadHandler(
        BlockService blockService,
        BookingPeopleCountHandler bookingPeopleCountHandler,
        BookingDateHandler bookingDateHandler, BookingTimeHandler bookingTimeHandler) {
        this.blockService = blockService;
        this.bookingPeopleCountHandler = bookingPeopleCountHandler;
        this.bookingDateHandler = bookingDateHandler;
        this.bookingTimeHandler = bookingTimeHandler;
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

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }
}
