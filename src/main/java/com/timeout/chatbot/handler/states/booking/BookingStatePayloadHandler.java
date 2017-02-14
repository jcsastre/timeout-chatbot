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

    @Autowired
    public BookingStatePayloadHandler(
        BlockService blockService,
        BookingPeopleCountHandler bookingPeopleCountHandler
    ) {
        this.blockService = blockService;
        this.bookingPeopleCountHandler = bookingPeopleCountHandler;
    }

    public void handle(
        String payloadAsString,
        Session session
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final JSONObject payload = new JSONObject(payloadAsString);
        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case booking_people_count:
                bookingPeopleCountHandler.handle(session, payload);
                break;

            case booking_date:
                //TODO
                break;

            default:
                blockService.sendErrorBlock(session.getUser());
                break;
        }
    }
}
