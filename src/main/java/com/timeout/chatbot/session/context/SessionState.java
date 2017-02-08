package com.timeout.chatbot.session.context;

public enum SessionState {
    UNDEFINED,
    SEARCH_SUGGESTIONS,
    DISCOVER,
    WHATS_NEW,
    MOST_LOVED,
    LOOKING, // looking around
    ITEM, // options about particular item
    BOOKING  // booking over an item
}
