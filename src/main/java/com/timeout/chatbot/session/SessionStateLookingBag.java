package com.timeout.chatbot.session;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.GraffittiWhat;

public class SessionStateLookingBag {

    private GraffittiWhat graffittiWhat;
    private String graffittiWhen;
    private String graffittiWhere;
    private GraffittiType graffittiType;
    private Integer graffittiPageNumber;
    private Integer reaminingItems;
    private SessionContextBag.Geolocation geolocation;
    private Double radius = 0.5D;

    public SessionStateLookingBag() {
        this.graffittiWhat = null;
        this.graffittiWhen = null;
        this.graffittiWhere = null;
        this.graffittiType = null;
        this.graffittiPageNumber = null;
    }

    public GraffittiWhat getGraffittiWhat() {
        return graffittiWhat;
    }

    public void setGraffittiWhat(GraffittiWhat graffittiWhat) {
        this.graffittiWhat = graffittiWhat;
    }

    public String getGraffittiWhen() {
        return graffittiWhen;
    }

    public void setGraffittiWhen(String graffittiWhen) {
        this.graffittiWhen = graffittiWhen;
    }

    public String getGraffittiWhere() {
        return graffittiWhere;
    }

    public void setGraffittiWhere(String graffittiWhere) {
        this.graffittiWhere = graffittiWhere;
    }

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }

    public void setGraffittiType(GraffittiType graffittiType) {
        this.graffittiType = graffittiType;
    }

    public Integer getGraffittiPageNumber() {
        return graffittiPageNumber;
    }

    public void setGraffittiPageNumber(Integer graffittiPageNumber) {
        this.graffittiPageNumber = graffittiPageNumber;
    }

    public Integer getReaminingItems() {
        return reaminingItems;
    }

    public void setReaminingItems(Integer reaminingItems) {
        this.reaminingItems = reaminingItems;
    }

    public SessionContextBag.Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(SessionContextBag.Geolocation geolocation) {
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
