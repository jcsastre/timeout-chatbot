package com.timeout.chatbot.domain.user;

import java.io.Serializable;

public class SuggestionsDone implements Serializable {

    private Boolean discover = false;
    private Boolean restaurantsFineSearch = false;

    @Override
    public String toString() {
        return String.format(
            "SuggestionsDone[discover=%s, restaurantsFineSearch=%s]",
            discover, restaurantsFineSearch
        );
    }
}
