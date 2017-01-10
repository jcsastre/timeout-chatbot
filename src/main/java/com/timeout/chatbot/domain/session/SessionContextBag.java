package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;

public class SessionContextBag {
    private CategoryPrimary categoryPrimary;

    private Geolocation geolocation;

    private Double radius = 0.5D;

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public class Geolocation {
        private Double latitude;
        private Double longitude;

        public Geolocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
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
    }
}
