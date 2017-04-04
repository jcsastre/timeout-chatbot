package com.timeout.chatbot.session.state;

import java.io.Serializable;

public enum BookingState implements Serializable {

    PROPOSAL,
    ASKING_DATE,
    ASKING_TIME,
    ASKING_PEOPLE

}
