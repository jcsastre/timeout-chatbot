package com.timeout.chatbot.block.deprecated;

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
public class PhoneCallBlock {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public PhoneCallBlock(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId,
        String phoneNumber,
        String venueName
    ) throws MessengerApiException, MessengerIOException {

        phoneNumber = "+34678750727";

        String text =
            String.format(
                "Are you sure you want to call %s?",
                venueName
            );

        messengerSendClient.sendTemplate(
            userId,
            ButtonTemplate.newBuilder(
                text,
                Button.newListBuilder()
                    .addCallButton(
                        "Call " + phoneNumber,
                        phoneNumber
                    ).toList()
                    .addPostbackButton(
                        "Cancel",
                        new JSONObject()
                            .put("type", PayloadType.cancel)
                            .toString()
                    ).toList()

                    .build()
            ).build()
        );
    }
}
