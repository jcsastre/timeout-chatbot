package com.timeout.chatbot.domain.session;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.graffitti.domain.response.facets.CategorySecondary;

public class SessionContextBag {

    private GraffittiType graffittiType;
    private CategoryPrimary categoryPrimary;
    private CategorySecondary categorySecondary;
    private Geolocation geolocation;
    private Double radius = 0.5D;
    private Integer reaminingItems;
    private Integer pageNumber;

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }

    public void setGraffittiType(GraffittiType graffittiType) {
        this.graffittiType = graffittiType;
    }

    public CategoryPrimary getCategoryPrimary() {
        return categoryPrimary;
    }

    public void setCategoryPrimary(CategoryPrimary categoryPrimary) {
        this.categoryPrimary = categoryPrimary;
    }

    public CategorySecondary getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(CategorySecondary categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

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

    public Integer getReaminingItems() {
        return reaminingItems;
    }

    public void setReaminingItems(Integer reaminingItems) {
        this.reaminingItems = reaminingItems;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }


    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
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
