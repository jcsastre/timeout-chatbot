package com.timeout.chatbot.handler.messenger.quickreply;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.timeout.chatbot.block.state.booking.BlockBookingAskDate;
import com.timeout.chatbot.block.state.booking.BlockBookingAskPeopleCount;
import com.timeout.chatbot.block.state.booking.BlockBookingAskTime;
import com.timeout.chatbot.block.state.booking.BlockBookingProposal;
import com.timeout.chatbot.domain.payload.QuickreplyPayload;
import com.timeout.chatbot.session.Session;
import com.timeout.chatbot.session.state.BookingState;
import com.timeout.chatbot.session.state.SessionState;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class QuickReplyMessageEventHandlerBooking {

    private final BlockBookingProposal blockBookingProposal;
    private final BlockBookingAskDate blockBookingAskDate;
    private final BlockBookingAskTime blockBookingAskTime;
    private final BlockBookingAskPeopleCount blockBookingAskPeopleCount;

    @Autowired
    public QuickReplyMessageEventHandlerBooking(
        BlockBookingProposal blockBookingProposal,
        BlockBookingAskDate blockBookingAskDate,
        BlockBookingAskTime blockBookingAskTime,
        BlockBookingAskPeopleCount blockBookingAskPeopleCount
    ) {
        this.blockBookingProposal = blockBookingProposal;
        this.blockBookingAskDate = blockBookingAskDate;
        this.blockBookingAskTime = blockBookingAskTime;
        this.blockBookingAskPeopleCount = blockBookingAskPeopleCount;
    }

    public void handle(
        JSONObject payload,
        Session session
    ) throws InterruptedException, MessengerApiException, MessengerIOException, IOException {

        final QuickreplyPayload payloadType = QuickreplyPayload.valueOf(payload.getString("type"));
        switch (payloadType) {

            case booking_proposal_ok:
                handleProposalOk(session);
                break;

            case booking_ask_day:
                handleAskDay(session);
                break;

            case booking_ask_time:
                handleAskTime(session);
                break;

            case booking_ask_people:
                handleAskPeople(session);
                break;

            case booking_update_day:
                handleUpdateDay(session, payload);
                break;

            case booking_update_time:
                handleUpdateTime(session, payload);

            case booking_update_people:
                handleUpdatePeople(session, payload);
                break;
        }
    }

    private void handleProposalOk(
        Session session
    ) {
        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.PROPOSAL
        ) {
            //TODO
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleAskDay(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.PROPOSAL
        ) {

            session.bagBooking.state = BookingState.ASKING_DATE;
            blockBookingAskDate.send(session.user.messengerId);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleAskTime(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.PROPOSAL
        ) {

            session.bagBooking.state = BookingState.ASKING_TIME;
            blockBookingAskTime.send(session.user.messengerId);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleAskPeople(
        Session session
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.PROPOSAL
        ) {

            session.bagBooking.state = BookingState.ASKING_PEOPLE;
            blockBookingAskPeopleCount.send(session.user.messengerId);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleUpdateDay(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.ASKING_DATE
        ) {
            session.bagBooking.localDate = LocalDate.parse(payload.getString("date"));
            session.bagBooking.state = BookingState.PROPOSAL;
            blockBookingProposal.send(session, false);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleUpdateTime(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.ASKING_TIME
        ) {
            session.bagBooking.localTime = LocalTime.of(new Integer(payload.getString("time")), 0);
            session.bagBooking.state = BookingState.PROPOSAL;
            blockBookingProposal.send(session, false);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private void handleUpdatePeople(
        Session session,
        JSONObject payload
    ) throws MessengerApiException, MessengerIOException {

        if (
            session.state == SessionState.BOOKING &&
            session.bagBooking.state == BookingState.ASKING_PEOPLE
        ) {
            session.bagBooking.peopleCount = payload.getInt("count");
            session.bagBooking.state = BookingState.PROPOSAL;
            blockBookingProposal.send(session, false);
        } else {
            //TODO: ha pasado mucho tiempo, y los resultados pueden ser distintos, que hacer?
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(QuickReplyMessageEventHandlerBooking.class);
}
