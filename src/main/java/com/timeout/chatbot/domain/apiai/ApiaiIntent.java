package com.timeout.chatbot.domain.apiai;

public enum ApiaiIntent {
    UNKOWN(null),
    GREETINGS("greetings"),
    FIND_CAMPINGS("findCampings"),
    FIND_OFFERS("findOffers"),
    SET_LOCATION("setLocation");

    private final String text;

    ApiaiIntent(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ApiaiIntent fromString(String intent) {
        if (intent == null) {
            return UNKOWN;
        }

        if (intent.equals(GREETINGS.toString())) {
            return GREETINGS;
        } else if (intent.equals(FIND_CAMPINGS.toString())) {
            return FIND_CAMPINGS;
        } else if (intent.equals(FIND_OFFERS.toString())) {
            return FIND_OFFERS;
        } else if (intent.equals(SET_LOCATION.toString())) {
            return SET_LOCATION;
        }

        return UNKOWN;
    }
}
