package com.timeout.chatbot.handler.states.booking;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.domain.nlu.NluException;
import com.timeout.chatbot.handler.intent.IntentService;
import com.timeout.chatbot.services.BlockService;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.bag.SessionStateBookingBag;
import com.timeout.chatbot.session.state.BookingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingStateTextHandler {

    private final IntentService intentService;
    private final MessengerSendClient msc;
    private final BlockService blockService;

    @Autowired
    public BookingStateTextHandler(
        IntentService intentService,
        MessengerSendClient msc,
        BlockService blockService
    ) {
        this.intentService = intentService;
        this.msc = msc;
        this.blockService = blockService;
    }

    public void handle(
        String text,
        Session session
    ) throws NluException, MessengerApiException, MessengerIOException, IOException, InterruptedException {

        final SessionStateBookingBag bookingBag = session.getSessionStateBookingBag();
        final BookingState bookingState = bookingBag.getBookingState();

        switch (bookingState) {

            case PEOPLE_COUNT:
                handePeopleCount(text, session);
                break;

            case DATE:
                //TODO
                break;

            case TIME:
                //TODO
                break;

            case CONFIRMATION_BOOKING_DETAILS:
                //TODO
                break;

            case FIRST_NAME:
                handleFirstName(text, session);
                break;

            case LAST_NAME:
                //TODO
                break;

            default:
                //TODO
        }
    }

    private void handePeopleCount(
        String text,
        Session session
    ) {
        //TODO
    }

    private void handleFirstName(
        String text,
        Session session
    ) {
        //TODO
    }
}
