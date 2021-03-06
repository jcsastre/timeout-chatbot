package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockBookingFbFirstNameConfirmation {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockBookingFbFirstNameConfirmation(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final String firstName = session.fbUserProfile.getFirstName();

        String msg = "Can I use '" + firstName + "' as your first name for the booking?";

        messengerSendClient.sendTextMessage(
            session.user.messengerId,
            msg,
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Yes",
            new JSONObject()
                .put("type", PayloadType.booking_first_name_fb_ok)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "No",
            new JSONObject()
                .put("type", PayloadType.booking_first_name_fb_not_ok)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
