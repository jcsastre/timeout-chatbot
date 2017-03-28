package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiSubcategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;

import java.io.Serializable;

public class SessionStateSearchingBag implements Serializable {

    public GraffittiCategory graffittiCategory;
    public GraffittiType graffittiType;

    public GraffittiSubcategory graffittiSubcategory;

    public Neighborhood neighborhood;
    public Geolocation geolocation;

    public Integer pageNumber;
    public Integer reaminingItems;

//    public Category category;
//    public Subcategory subcategory;

//    private What what;
//    private GraffittiFacetV4FacetNode graffittiWhatCategoryNode;

//    public String graffittiWhen;
//    public Integer graffittiPageNumber;
//
//    public Double radius = 0.5D;

    public SessionStateSearchingBag() {
        this.graffittiCategory = null;
        this.graffittiSubcategory = null;
        this.graffittiType = null;
        this.neighborhood = null;
        this.geolocation = null;
        this.pageNumber = 1;
        this.reaminingItems = 0;
    }

    @Override
    public String toString() {
        return String.format(
            "SessionStateSearchingBag {" +
                "graffittiCategory=%s, " +
                "graffittiType=%s, " +
                "graffittiSubcategory=%s, " +
                "neighborhood=%s, " +
                "geolocation=%s, " +
                "pageNumber=%s" +
            "}",
            graffittiCategory,
            graffittiType,
            graffittiSubcategory != null ? graffittiSubcategory.name : "<unset>",
            neighborhood != null ? neighborhood.name : "<unset>",
            geolocation != null ? "<set>" : "<unset>",
            pageNumber
        );
    }
}
