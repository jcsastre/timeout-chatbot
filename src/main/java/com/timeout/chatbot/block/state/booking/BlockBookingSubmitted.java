package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class BlockBookingSubmitted {

    private final MessengerSendClient msc;
    private final CloudinaryUrlBuilder cloudinaryUrlBuilder;

    @Autowired
    public BlockBookingSubmitted(
        MessengerSendClient msc,
        CloudinaryUrlBuilder cloudinaryUrlBuilder
    ) {
        this.msc = msc;
        this.cloudinaryUrlBuilder = cloudinaryUrlBuilder;
    }

    public void send(
        Session session
    ) throws MessengerApiException, MessengerIOException, UnsupportedEncodingException {

        final String userMessengerId = session.user.messengerId;

        msc.sendTextMessage(
            userMessengerId,
            "Your booking has been submitted"
        );

        final SessionStateBookingBag bagBooking = session.bagBooking;
        final SessionStateItemBag bagItem = session.bagItem;
        final Venue venue = bagItem.venue;

        String receiptImageUrl =
            cloudinaryUrlBuilder.buildBookReceiptUrl(
                bagBooking.peopleCount,
                bagBooking.localDate,
                bagBooking.localTime,
                venue.getMainImage().id,
                venue.name,
                venue.address1,
                venue.city,
                venue.postCode
            );

        msc.sendTextMessage(
            userMessengerId,
            "Below you can see a receipt of your book. Share it with your companions."
        );

        msc.sendImageAttachment(
            userMessengerId,
            receiptImageUrl,
            buildQuickReplies()
        );
    }

    public List<QuickReply> buildQuickReplies() {

        final QuickReply.ListBuilder listBuilder = QuickReply.newListBuilder();

        listBuilder.addTextQuickReply(
            "Continue",
            new JSONObject()
                .put("type", QuickreplyPayload.booking_back_to_discover)
                .toString()
        ).toList();

        return listBuilder.build();
    }
}
