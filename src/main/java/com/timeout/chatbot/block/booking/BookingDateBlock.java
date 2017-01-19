package com.timeout.chatbot.block.booking;

import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class BookingDateBlock {
    private final MessengerSendClientWrapper messengerSendClientWrapper;

    @Autowired
    public BookingDateBlock(
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

        final LocalDate today = LocalDate.now();
        addTextQuickReply(
            "Today",
            today,
            listBuilder
        );

        final LocalDate tomorrow = today.plusDays(1);
        addTextQuickReply(
            "Tomorrow",
            tomorrow,
            listBuilder
        );

        final LocalDate tomorrowPlus1 = tomorrow.plusDays(1);
        addTextQuickReply(
            "next " + tomorrowPlus1.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
            tomorrowPlus1,
            listBuilder
        );

        final LocalDate tomorrowPlus2 = tomorrow.plusDays(2);
        addTextQuickReply(
            "next " + tomorrowPlus2.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
            tomorrowPlus2,
            listBuilder
        );

        final LocalDate tomorrowPlus3 = tomorrow.plusDays(3);
        addTextQuickReply(
            "next " + tomorrowPlus3.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
            tomorrowPlus3,
            listBuilder
        );

        final LocalDate tomorrowPlus4 = tomorrow.plusDays(4);
        addTextQuickReply(
            "next " + tomorrowPlus4.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
            tomorrowPlus4,
            listBuilder
        );

        final LocalDate tomorrowPlus5 = tomorrow.plusDays(5);
        addTextQuickReply(
            "next " + tomorrowPlus5.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
            tomorrowPlus5,
            listBuilder
        );

        return listBuilder.build();
    }

    private void addTextQuickReply(
        String title,
        LocalDate localDate,
        QuickReply.ListBuilder listBuilder
    ) {

        listBuilder.addTextQuickReply(
            title,
            new JSONObject()
                .put("type", PayloadType.booking_date)
                .put("date", localDate.toString())
                .toString()
        ).toList();
    }
}
