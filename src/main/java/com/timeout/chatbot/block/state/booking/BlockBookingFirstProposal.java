package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.session.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockBookingFirstProposal {

    private final MessengerSendClient msc;

    @Autowired
    public BlockBookingFirstProposal(
        MessengerSendClient msc
    ) {
        this.msc = msc;
    }

    public void send(
        Session session

    ) throws MessengerApiException, MessengerIOException {

        String msg = "What about tomorrow at 13:00 for 2 people?";

        msc.sendTextMessage(
            session.user.messengerId,
            msg,
            buildQuickReplies()
        );
    }

    private List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Great, let's go",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_first_proposal_ok)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Chnage day",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_change_day)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Change time",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_change_time)
                .toString()
        ).toList();

        listBuilder.addTextQuickReply(
            "Change people",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_change_people)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
