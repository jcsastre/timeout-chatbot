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

    @Override
    public String toString() {
        String subcategory = null;
        if (graffittiSubcategory != null) {
            subcategory = graffittiSubcategory.name;
        }

        return String.format(
            "SessionStateSearchingBag {graffittiCategory=%s, graffittiType=%s, graffittiSubcategory=%s, pageNumber=%s}",
            graffittiCategory,
            graffittiType,
            subcategory,
            pageNumber
        );
    }
}
