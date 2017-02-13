package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.ButtonTemplate;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        String userId,
        String userFirstName
    ) {

        messengerSendClientWrapper.sendTemplate(
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
