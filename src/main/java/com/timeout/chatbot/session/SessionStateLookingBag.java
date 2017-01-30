package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;

public class SessionStateLookingBag {

    private What what;
    private GraffittiFacetV5Node graffittiCategory;
    private GraffittiFacetV5Node graffittiWhatNode;

    private String graffittiWhen;
    private GraffittiFacetV4FacetNode graffittiWhere;
    private GraffittiType graffittiType;
    private Integer graffittiPageNumber;
    private Integer reaminingItems;
    private Geolocation geolocation;
    private Double radius = 0.5D;

    public SessionStateLookingBag() {
        this.what = null;
        this.graffittiWhen = null;
        this.graffittiWhere = null;
        this.graffittiType = null;
        this.graffittiPageNumber = null;
    }

    public What getWhat() {
        return what;
    }

    public void setWhat(What what) {
        this.what = what;
    }

    public GraffittiFacetV5Node getGraffittiWhatNode() {
        return graffittiWhatNode;
    }

    public void setGraffittiWhatNode(GraffittiFacetV5Node graffittiWhatNode) {
        this.graffittiWhatNode = graffittiWhatNode;
    }

    public String getGraffittiWhen() {
        return graffittiWhen;
    }

    public void setGraffittiWhen(String graffittiWhen) {
        this.graffittiWhen = graffittiWhen;
    }

    public GraffittiFacetV4FacetNode getGraffittiWhere() {
        return graffittiWhere;
    }

    public void setGraffittiWhere(GraffittiFacetV4FacetNode graffittiWhere) {
        this.geolocation = null;
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

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.graffittiPageNumber = 1;
        this.graffittiWhere = null;
        this.geolocation = geolocation;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
