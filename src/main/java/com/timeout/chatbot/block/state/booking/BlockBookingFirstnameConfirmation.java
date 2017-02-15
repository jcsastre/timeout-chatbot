package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlockBookingFirstnameConfirmation {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockBookingFirstnameConfirmation(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId,
        String userFirstName
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTemplate(
            userId,
            ButtonTemplate.newBuilder(
                "Can I use '" + userFirstName + "' as your first name for the booking?",
                Button.newListBuilder()
                    .addPostbackButton(
                        "Yes",
                        new JSONObject()
                            .put("type", PayloadType.booking_first_name_fb_ok)
                            .toString()
                    ).toList()
                    .addPostbackButton(
                        "No",
                        new JSONObject()
                            .put("type", PayloadType.booking_first_name_fb_not_ok)
                            .toString()
                    ).toList()

                    .build()
            ).build()
        );
    }
}
