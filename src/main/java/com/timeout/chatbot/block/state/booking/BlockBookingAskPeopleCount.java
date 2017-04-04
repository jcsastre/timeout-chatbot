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
public class BlockBookingAskPeopleCount {

    private final MessengerSendClient msc;

    @Autowired
    public BlockBookingAskPeopleCount(
        MessengerSendClient messengerSendClientWrapper
    ) {
        this.msc = messengerSendClientWrapper;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        msc.sendTextMessage(
            userId,
            "Please, press or type the number of people",
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        for (int i=1; i<=10; i++) {
            listBuilder.addTextQuickReply(
                Integer.toString(i),
                new JSONObject()
                    .put("type", QuickreplyPayload.booking_update_people)
                    .put("count", i)
                    .toString()
            ).toList();
        }

        return listBuilder.build();
    }
}
