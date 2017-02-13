package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingTimeBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public BookingTimeBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTextMessage(
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
                .put("type", PayloadType.booking_time)
                .put("time", "13")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "14:00",
            new JSONObject()
                .put("type", PayloadType.booking_time)
                .put("time", "14")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "19:00",
            new JSONObject()
                .put("type", PayloadType.booking_time)
                .put("time", "19")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "20:00",
            new JSONObject()
                .put("type", PayloadType.booking_time)
                .put("time", "20")
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
