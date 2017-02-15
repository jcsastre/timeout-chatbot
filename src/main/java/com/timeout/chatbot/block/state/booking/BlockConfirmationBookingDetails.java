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

import java.time.LocalDate;
import java.time.LocalTime;

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

        messengerSendClient.sendTemplate(
            userId,
            buildButtonTemplate(peopleCount, localDate, localTime)
        );
    }

    private ButtonTemplate buildButtonTemplate(
        Integer peopleCount,
        LocalDate localDate,
        LocalTime localTime
    ) {
        String msg =
            "Is that booking information correct?:\n" +
                "\n" +
                "People: " + peopleCount + "\n" +
                "Date: " + localDate + "\n" +
                "Time: " + localTime;

        return ButtonTemplate.newBuilder(
            msg,
            Button.newListBuilder()
                .addPostbackButton(
                    "Yes",
                    new JSONObject()
                        .put("type", PayloadType.booking_info_ok)
                        .toString()
                ).toList()
                .addPostbackButton(
                    "No",
                    new JSONObject()
                        .put("type", PayloadType.booking_info_not_ok)
                        .toString()
                ).toList()

                .build()
        ).build();
    }
}
