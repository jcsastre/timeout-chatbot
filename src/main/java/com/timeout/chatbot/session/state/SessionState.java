package com.timeout.chatbot.session.state;

import java.io.Serializable;

public enum SessionState implements Serializable {

    UNDEFINED,
    SEARCH_SUGGESTIONS,
    DISCOVER,
    MOST_LOVED,
    SEARCHING,
    ITEM,
    SUBMITTING_REVIEW,
    BOOKING
}
