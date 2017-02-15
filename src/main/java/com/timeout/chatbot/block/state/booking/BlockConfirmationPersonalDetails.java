package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockConfirmationPersonalDetails {
    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockConfirmationPersonalDetails(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId,
        String firstName,
        String lastName,
        String email,
        String phone
    ) throws MessengerApiException, MessengerIOException {

        String msg =
            "Is that information correct?:\n" +
                "\n" +
                "First name: " + firstName + "\n" +
                "Last name: " + lastName + "\n" +
                "Email: " + email + "\n" +
                "Phone number: " + phone;

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
                .put("type", PayloadType.booking_personal_info_ok)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "No",
            new JSONObject()
                .put("type", PayloadType.booking_personal_info_not_ok)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
