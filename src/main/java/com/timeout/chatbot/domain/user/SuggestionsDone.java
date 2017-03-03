package com.timeout.chatbot.domain.user;

import java.io.Serializable;

public class SuggestionsDone implements Serializable {

    private Boolean discover = false;
    private Boolean restaurantsFineSearch = false;

    public SuggestionsDone() {
    }

    public Boolean getDiscover() {
        return discover;
    }

    public void setDiscover(Boolean discover) {
        this.discover = discover;
    }

    public Boolean getRestaurantsFineSearch() {
        return restaurantsFineSearch;
    }

    public void setRestaurantsFineSearch(Boolean restaurantsFineSearch) {
        this.restaurantsFineSearch = restaurantsFineSearch;
    }
}
