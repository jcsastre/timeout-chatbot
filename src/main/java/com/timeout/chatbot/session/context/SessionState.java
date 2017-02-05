package com.timeout.chatbot.session.context;

public enum SessionState {
    UNDEFINED,
    WELCOMED,
    LOOKING, // looking around
    ITEM, // options about particular item
    BOOKING  // booking over an item
}
