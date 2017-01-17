package com.timeout.chatbot.block.booking;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingPeopleCountBlock{
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public BookingPeopleCountBlock(
        MessengerSendClientWrapper messengerSendClientWrapper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
    }

    public void send(
        String userId
    ) {
        messengerSendClientWrapper.sendTextMessage(
            userId,
            "How many people? You can also type the number",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (int i=1; i<=10; i++) {
            listBuilder.addTextQuickReply(
                Integer.toString(i),
                new JSONObject()
                    .put("type", PayloadType.booking_people_count)
                    .put("count", Integer.toString(i))
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
