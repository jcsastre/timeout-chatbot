package com.timeout.chatbot.domain;

public class UserSuggestionsDone {
    public Boolean restaurantsFineSearch = false;

    public UserSuggestionsDone() {
        this.restaurantsFineSearch = false;
    }

    @Override
    public String toString() {
        return String.format(
            "SuggestionsDone[restaurantsFineSearch=%s]",
            restaurantsFineSearch.toString()
        );
    }
}
