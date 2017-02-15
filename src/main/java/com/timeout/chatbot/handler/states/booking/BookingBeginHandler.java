package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.block.state.booking.BlockBookingPeopleCount;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.bag.SessionStateItemBag;
import com.timeout.chatbot.session.state.BookingState;
import com.timeout.chatbot.session.state.SessionState;
import org.springframework.stereotype.Component;

@Component
public class BookingBeginHandler {

    private final MessengerSendClient messengerSendClient;
    private final BlockService blockService;
    private final BlockBookingPeopleCount blockBookingPeopleCount;

    public BookingBeginHandler(
        MessengerSendClient messengerSendClient,
        BlockService blockService,
        BlockBookingPeopleCount blockBookingPeopleCount
    ) {
        this.messengerSendClient = messengerSendClient;
        this.blockService = blockService;
        this.blockBookingPeopleCount = blockBookingPeopleCount;
    }

    public void handle(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        final SessionStateItemBag itemBag = session.getSessionStateItemBag();

        final GraffittiType graffittiType = itemBag.getGraffittiType();
        if (graffittiType == GraffittiType.VENUE) {

            session.setSessionState(SessionState.BOOKING);
            final SessionStateBookingBag bookingBag = session.getSessionStateBookingBag();
            bookingBag.setBookingState(BookingState.PEOPLE_COUNT);
            blockBookingPeopleCount.send(session.getUser().getMessengerId());

        } else {

            messengerSendClient.sendTextMessage(
                session.getUser().getMessengerId(),
                "Sorry, 'Book' feature is not implemented yet"
            );
        }
    }
}
