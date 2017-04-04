package com.timeout.chatbot.graffitti.domain;

import java.io.Serializable;

public enum GraffittiType implements Serializable {

    VENUE("venue"),
    EVENT("event"),
    FILM("film"),
    PAGE("page");

    private final String value;

    GraffittiType(String value) {
        this.value = value;
    }

    public static GraffittiType fromValue(String value) {
        if (value != null) {
            for (GraffittiType graffittiType : values()) {
                if (graffittiType.value.equals(value)) {
                    return graffittiType;
                }
            }
        }

        throw new IllegalArgumentException("Invalid color: " + value);
    }

    public String toValue() {
        return value;
    }

//    @Override
//    public String toString() {
//        return String.format(
//            "GraffittiType=%s",
//            value
//        );
//    }

    ////////// 2st implementation (part of code)


//    public static GraffittiType fromTypeAsString(
//        String typeAsString
//    ) {
//        if (typeAsString.equalsIgnoreCase("VENUE")) {
//            return VENUE;
//        } else if (typeAsString.equalsIgnoreCase("EVENT")) {
//            return EVENT;
//        } else if (typeAsString.equalsIgnoreCase("FILM")) {
//            return FILM;
//        } else if (typeAsString.equalsIgnoreCase("PAGE")) {
//            return PAGE;
//        }
//
//        return null;
//    }

    ////////// 1st implementation

//    VENUE("VENUE"),
//    EVENT("EVENT"),
//    FILM("FILM"),
//    PAGE("PAGE");
//
//    private String value;
//    GraffittiType(String value) {
//        this.value = value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    @Override
//    public String toString() {
//        return value;
//    }
//
//    public static GraffittiType fromString(String type) {
//
//        if (type == null) {
//            return null;
//        }
//
//        if (type.equalsIgnoreCase(VENUE.toString())) {
//            return VENUE;
//        } else if (type.equalsIgnoreCase(EVENT.toString())) {
//            return EVENT;
//        } else if (type.equalsIgnoreCase(FILM.toString())) {
//            return FILM;
//        } else if (type.equalsIgnoreCase(PAGE.toString())) {
//            return PAGE;
//        }
//
//        return null;
//    }
}
