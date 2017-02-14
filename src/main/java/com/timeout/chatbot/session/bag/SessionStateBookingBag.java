package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.session.state.BookingState;

import java.time.LocalDate;

public class SessionStateBookingBag {

    private BookingState bookingState;
    private Integer peopleCount;
    private LocalDate localDate;

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
