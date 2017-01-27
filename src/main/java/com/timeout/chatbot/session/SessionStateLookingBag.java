package com.timeout.chatbot.session;

import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.What;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.domain.response.facets.v5.GraffittiFacetV5Node;

public class SessionStateLookingBag {

    private What what;
    private GraffittiFacetV5Node graffittiFacetV5WhatV5Node;

    private String graffittiWhen;
    private String graffittiWhere;
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

    public GraffittiFacetV5Node getGraffittiFacetV5WhatV5Node() {
        return graffittiFacetV5WhatV5Node;
    }

    public void setGraffittiFacetV5WhatV5Node(GraffittiFacetV5Node graffittiFacetV5WhatV5Node) {
        this.graffittiFacetV5WhatV5Node = graffittiFacetV5WhatV5Node;
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
