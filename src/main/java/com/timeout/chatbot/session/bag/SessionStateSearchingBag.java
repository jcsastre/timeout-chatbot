package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.entities.Category;
import com.timeout.chatbot.domain.entities.Subcategory;

import java.io.Serializable;

public class SessionStateSearchingBag implements Serializable {

    private Category category;
    private Subcategory subcategory;

//    private What what;
//    private GraffittiFacetV4FacetNode graffittiWhatCategoryNode;

    private String graffittiWhen;

    private Integer graffittiPageNumber;
    private Integer reaminingItems;

    private Neighborhood neighborhood;
    private Geolocation geolocation;
    private Double radius = 0.5D;

    public SessionStateSearchingBag() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getGraffittiWhen() {
        return graffittiWhen;
    }

    public void setGraffittiWhen(String graffittiWhen) {
        this.graffittiWhen = graffittiWhen;
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
        this.neighborhood = null;
        this.geolocation = geolocation;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.graffittiPageNumber = 1;
        this.geolocation = null;
        this.neighborhood = neighborhood;
    }
}
