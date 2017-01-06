package com.timeout.chatbot.platforms.messenger.payload;

public enum PayloadType {
    GET_STARTED("get_started");

    private final String value;

    PayloadType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
