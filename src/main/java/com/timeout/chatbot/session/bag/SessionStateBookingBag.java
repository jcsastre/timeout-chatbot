package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.session.state.BookingState;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionStateBookingBag implements Serializable {

    public BookingState state;
    public Integer peopleCount;
    public LocalDate localDate;
    public LocalTime localTime;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;

    public SessionStateBookingBag() {
        this.state = BookingState.PROPOSAL;
        this.peopleCount = 2;
        this.localDate = LocalDate.now().plusDays(1);
        this.localTime = LocalTime.of(14, 0);
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phone = null;
    }
}
