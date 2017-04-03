package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class BlockBookingAskDate {

    private final MessengerSendClient messengerSendClient;

    @Autowired
    public BlockBookingAskDate(
        MessengerSendClient messengerSendClient
    ) {
        this.messengerSendClient = messengerSendClient;
    }

    public void send(
        String userId
    ) throws MessengerApiException, MessengerIOException {

        messengerSendClient.sendTextMessage(
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
            buildText(tomorrowPlus1),
            tomorrowPlus1,
            listBuilder
        );

        final LocalDate tomorrowPlus2 = tomorrow.plusDays(2);
        addTextQuickReply(
            buildText(tomorrowPlus2),
            tomorrowPlus2,
            listBuilder
        );

        final LocalDate tomorrowPlus3 = tomorrow.plusDays(3);
        addTextQuickReply(
            buildText(tomorrowPlus3),
            tomorrowPlus3,
            listBuilder
        );

        final LocalDate tomorrowPlus4 = tomorrow.plusDays(4);
        addTextQuickReply(
            buildText(tomorrowPlus4),
            tomorrowPlus4,
            listBuilder
        );

        final LocalDate tomorrowPlus5 = tomorrow.plusDays(5);
        addTextQuickReply(
            buildText(tomorrowPlus5),
            tomorrowPlus5,
            listBuilder
        );

        return listBuilder.build();
    }

    private String buildText(
        LocalDate localDate
    ) {
        return
            String.format(
                "%s (%s)",
                localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
                localDate.format(DateTimeFormatter.ofPattern("d MMM"))
            );
    }

    private void addTextQuickReply(
        String title,
        LocalDate localDate,
        QuickReply.ListBuilder listBuilder
    ) {

        listBuilder.addTextQuickReply(
            title,
            new JSONObject()
                .put("type", QuickreplyPayload.booking_update_day)
                .put("date", localDate.toString())
                .toString()
        ).toList();
    }
}
