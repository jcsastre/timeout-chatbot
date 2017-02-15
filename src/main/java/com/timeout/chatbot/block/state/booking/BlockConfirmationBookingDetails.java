package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class BlockConfirmationBookingDetails {
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockConfirmationBookingDetails(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId,
        Integer peopleCount,
        LocalDate localDate,
        LocalTime localTime

    ) throws MessengerApiException, MessengerIOException {

        String msg =
            "Is that booking information correct?:\n" +
                "\n" +
                "People: " + peopleCount + "\n" +
                "Date: " + localDate + "\n" +
                "Time: " + localTime;

        messengerSendClient.sendTextMessage(
            userId,
            msg,
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Yes",
            new JSONObject()
                .put("type", PayloadType.booking_info_ok)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "No",
            new JSONObject()
                .put("type", PayloadType.booking_info_not_ok)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
