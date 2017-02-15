package com.timeout.chatbot.handler.states.item;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.BlockError;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.handler.states.booking.BookingBeginHandler;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ItemStatePayloadHandler {

    private final BlockError blockError;
    private final BookingBeginHandler bookingBeginHandler;

    @Autowired
    public ItemStatePayloadHandler(
        BlockError blockError,
        BookingBeginHandler bookingBeginHandler
    ) {
        this.blockError = blockError;
        this.bookingBeginHandler = bookingBeginHandler;
    }

    public void handle(
        Session session,
        JSONObject payload
    ) throws NluException, MessengerIOException, MessengerApiException, IOException, InterruptedException {

        final PayloadType payloadType = PayloadType.valueOf(payload.getString("type"));

        switch (payloadType) {

            case book:
                bookingBeginHandler.handle(session);
                break;

            default:
                blockError.send(session.getUser());
                break;
        }
    }
}
