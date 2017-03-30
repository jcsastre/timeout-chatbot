package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.session.state.BookingState;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionStateBookingBag implements Serializable {

    public BookingState bookingState;
    public Integer peopleCount;
    public LocalDate localDate;
    public LocalTime localTime;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;

    public SessionStateBookingBag() {
        this.bookingState = BookingState.FIRST_PROPOSAL;
        this.peopleCount = null;
        this.localDate = null;
        this.localTime = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phone = null;
    }
}
