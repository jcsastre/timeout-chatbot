package com.timeout.chatbot.graffitti.domain;

public enum GraffittiType {
    VENUE("venue"),
    EVENT("event"),
    FILM("film");

    private final String value;
    GraffittiType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
