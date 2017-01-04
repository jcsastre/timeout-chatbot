package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;

public class SessionContextBag {
    private CategoryPrimary categoryPrimary;

    private String restaurantsFilterCuisine;

    private Double latitude;
    private Double longitude;
    private Double radius = 0.5D;

    public String getRestaurantsFilterCuisine() {
        return restaurantsFilterCuisine;
    }

    public void setRestaurantsFilterCuisine(String restaurantsFilterCuisine) {
        this.restaurantsFilterCuisine = restaurantsFilterCuisine;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
