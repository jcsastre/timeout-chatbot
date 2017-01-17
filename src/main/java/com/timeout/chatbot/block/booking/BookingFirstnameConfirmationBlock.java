package com.timeout.chatbot.block.booking;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingFirstnameConfirmationBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public BookingFirstnameConfirmationBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userId,
            "When are you coming? You can type stuff like 'tomorrow', 'next Wednesday', 'Thursday 20th', etc",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Today",
            new JSONObject()
                .put("type", PayloadType.booking_date)
                .put("date", "today")
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Tomorrow",
            new JSONObject()
                .put("type", PayloadType.booking_date)
                .put("date", "tomorrow")
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
