package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.domain.apiai.ApiaiIntent;

public enum Intent {
    UNKOWN(null),
    GREETINGS("greetings"),
    FIND_CAMPINGS("findCampings"),
    FIND_OFFERS("findOffers"),
    SET_LOCATION("setLocation");

    private final String text;

    Intent(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Intent fromApiaiIntent(ApiaiIntent apiaiIntent) {
        if (apiaiIntent == ApiaiIntent.GREETINGS) {
            return GREETINGS;
        } else if (apiaiIntent == ApiaiIntent.FIND_CAMPINGS) {
            return FIND_CAMPINGS;
        } else if (apiaiIntent == ApiaiIntent.FIND_OFFERS) {
            return FIND_OFFERS;
        } else if (apiaiIntent == ApiaiIntent.SET_LOCATION) {
            return SET_LOCATION;
        }

        return UNKOWN;
    }
}
