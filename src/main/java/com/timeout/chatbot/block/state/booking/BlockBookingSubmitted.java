package com.timeout.chatbot.block.state.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.cloudinary.CloudinaryUrlBuilder;
import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

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

        final SessionStateBookingBag bookingBag = session.bagBooking;
        final SessionStateItemBag itemBag = session.bagItem;
        final Venue venue = itemBag.venue;

        String receiptImageUrl =
            cloudinaryUrlBuilder.buildBookReceiptUrl(
                bookingBag.getPeopleCount(),
                bookingBag.getLocalDate(),
                bookingBag.getLocalTime(),
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
            receiptImageUrl
        );
    }
}
