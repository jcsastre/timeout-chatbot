package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class BlockBookingProposal {

    private final MessengerSendClient msc;

    @Autowired
    public BlockBookingProposal(
        MessengerSendClient msc
    ) {
        this.msc = msc;
    }

    public void send(
        Session session,
        boolean isFirstTime
    ) throws MessengerApiException, MessengerIOException {

        msc.sendTextMessage(
            session.user.messengerId,
            buildText(session.bagBooking, isFirstTime),
            buildQuickReplies()
        );
    }

    private String buildText(
        SessionStateBookingBag bag,
        boolean isFirstTime
    ) {
        String template = "%s (%s) at %s for %s people?";
        if (isFirstTime) {
            template = "What about %s (%s) at %s for %s people?";
        }

        return
            String.format(
                template,
                bag.localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
                bag.localDate.format(DateTimeFormatter.ofPattern("d MMM")),
                bag.localTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                bag.peopleCount
            );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Perfect",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_proposal_ok)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Change day",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_ask_day)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Change time",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_ask_time)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Change people",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_ask_people)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
