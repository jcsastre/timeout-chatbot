package com.timeout.chatbot.graffitti.domain;

import java.io.Serializable;

public enum GraffittiType implements Serializable {

    venue,
    event,
    film,
    page;

    public static GraffittiType fromTypeAsString(
        String typeAsString
    ) {
        if (typeAsString.equalsIgnoreCase("venue")) {
            return venue;
        } else if (typeAsString.equalsIgnoreCase("event")) {
            return event;
        } else if (typeAsString.equalsIgnoreCase("film")) {
            return film;
        } else if (typeAsString.equalsIgnoreCase("page")) {
            return page;
        }

        return null;
    }

//    venue("venue"),
//    event("event"),
//    film("film"),
//    page("page");
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
//        if (type.equalsIgnoreCase(venue.toString())) {
//            return venue;
//        } else if (type.equalsIgnoreCase(event.toString())) {
//            return event;
//        } else if (type.equalsIgnoreCase(film.toString())) {
//            return film;
//        } else if (type.equalsIgnoreCase(page.toString())) {
//            return page;
//        }
//
//        return null;
//    }
}
