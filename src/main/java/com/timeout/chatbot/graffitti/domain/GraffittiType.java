package com.timeout.chatbot.graffitti.domain;

public enum GraffittiType {
    VENUE("venue"),
    EVENT("event"),
    FILM("film");

    private final String value;
    GraffittiType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static GraffittiType fromString(String type) {
        if (type == null) {
            return null;
        }

        if (type.equalsIgnoreCase(VENUE.toString())) {
            return VENUE;
        } else if (type.equalsIgnoreCase(EVENT.toString())) {
            return EVENT;
        } else if (type.equalsIgnoreCase(FILM.toString())) {
            return FILM;
        }

        return null;
    }
}
