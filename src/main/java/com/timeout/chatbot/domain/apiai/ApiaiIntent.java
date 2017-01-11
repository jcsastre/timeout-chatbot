package com.timeout.chatbot.domain.apiai;

public enum ApiaiIntent {
    UNKOWN(null),
    GREETINGS("greetings"),
    FIND_THINGSTODO("findThingsToDo"),
    FIND_RESTAURANTS("restaurants"),
    FIND_BARSANDPUBS("findBarsAndPubs"),
    FIND_BARSANDPUBS_NEARBY("findBarsAndPubsNearby"),
    FIND_ART("findArt"),
    FIND_THEATRE("findTheatre"),
    FIND_MUSIC("findMusic"),
    FIND_NIGHTLIFE("findNightlife"),
    SET_LOCATION("setGeolocation"),
    FINDS_FILMS("findFilms"),
    DISCOVER("discover");

    private final String action;

    ApiaiIntent(final String action) {
        this.action = action;
    }

    public String toApiaiAction() {
        return action;
    }

    public static ApiaiIntent fromApiaiAction(String action) {
        if (action == null) {
            return UNKOWN;
        }

        if (action.equals(GREETINGS.toApiaiAction())) {
            return GREETINGS;
        } else if (action.equals(FIND_THINGSTODO.toApiaiAction())) {
            return FIND_THINGSTODO;
        } else if (action.equals(FIND_RESTAURANTS.toApiaiAction())) {
            return FIND_RESTAURANTS;
        } else if (action.equals(FIND_BARSANDPUBS.toApiaiAction())) {
            return FIND_BARSANDPUBS;
        } else if (action.equals(FIND_BARSANDPUBS_NEARBY.toApiaiAction())) {
            return FIND_BARSANDPUBS_NEARBY;
        } else if (action.equals(FIND_ART.toApiaiAction())) {
            return FIND_ART;
        } else if (action.equals(FIND_THEATRE.toApiaiAction())) {
            return FIND_THEATRE;
        } else if (action.equals(FIND_MUSIC.toApiaiAction())) {
            return FIND_MUSIC;
        } else if (action.equals(FIND_NIGHTLIFE.toApiaiAction())) {
            return FIND_NIGHTLIFE;
        } else if (action.equals(SET_LOCATION.toApiaiAction())) {
            return SET_LOCATION;
        } else if (action.equals(FINDS_FILMS.toApiaiAction())) {
            return FINDS_FILMS;
        } else if (action.equals(DISCOVER.toApiaiAction())) {
            return DISCOVER;
        }

        return UNKOWN;
    }
}
