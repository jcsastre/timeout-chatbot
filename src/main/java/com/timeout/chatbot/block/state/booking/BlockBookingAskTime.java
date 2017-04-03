package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockBookingAskTime {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockBookingAskTime(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
            userId,
            "Available times for that day",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "13:00",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_update_time)
                .put("time", "13")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "14:00",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_update_time)
                .put("time", "14")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "19:00",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_update_time)
                .put("time", "19")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "20:00",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_update_time)
                .put("time", "20")
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
