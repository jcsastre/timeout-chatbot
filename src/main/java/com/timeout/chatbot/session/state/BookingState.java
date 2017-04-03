package com.timeout.chatbot.session.state;

import java.io.Serializable;

public enum BookingState implements Serializable {

    PROPOSAL,
    ASKING_DATE,
    ASKING_TIME,
    ASKING_PEOPLE,

    PEOPLE_COUNT,
    DATE,
    TIME,
    CONFIRMATION_BOOKING_DETAILS,
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    PHONE,
    CONFIRMATION_PERSONAL_DETAILS,
    SAVE_INFO,
    CONFIRMATION
}
