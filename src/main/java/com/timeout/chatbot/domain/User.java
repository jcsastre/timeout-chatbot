package com.timeout.chatbot.domain;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    public String id;

    public String messengerId;

    private FbUserProfile fbUserProfile;

    public SuggestionsDone suggestionsDone;

    public User(String messengerId) {
        this.messengerId = messengerId;
        this.suggestionsDone = new SuggestionsDone();
    }

    public String getMessengerId() {
        return messengerId;
    }

    public FbUserProfile getFbUserProfile() {
        return fbUserProfile;
    }

    public void setFbUserProfile(FbUserProfile fbUserProfile) {
        this.fbUserProfile = fbUserProfile;
    }

    public SuggestionsDone getSuggestionsDone() {
        return suggestionsDone;
    }

    @Override
    public String toString() {
        return String.format(
            "User[id=%s, messengerId=%s, fbUserProfile=%s, suggestionsDone=%s]",
            id, messengerId, fbUserProfile.toString(), suggestionsDone.toString()
        );
    }

    public class SuggestionsDone {
        public Boolean restaurantsFineSearch = false;

        public SuggestionsDone() {
            this.restaurantsFineSearch = false;
        }

        @Override
        public String toString() {
            return String.format(
                "SuggestionsDone[fbUserProfile=%s, restaurantsFineSearch=%s]",
                fbUserProfile.toString(), restaurantsFineSearch.toString()
            );
        }

        public Boolean getRestaurantsFineSearch() {
            return restaurantsFineSearch;
        }

        public void setRestaurantsFineSearch(Boolean restaurantsFineSearch) {
            this.restaurantsFineSearch = restaurantsFineSearch;
        }
    }
}
