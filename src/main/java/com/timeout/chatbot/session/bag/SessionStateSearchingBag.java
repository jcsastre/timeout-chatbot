package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.domain.Geolocation;
import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.domain.What;

import java.io.Serializable;

public class SessionStateSearchingBag implements Serializable {

    public What what;

//    public Category category;
//    public Subcategory subcategory;

//    private What what;
//    private GraffittiFacetV4FacetNode graffittiWhatCategoryNode;

    public String graffittiWhen;
    public Integer graffittiPageNumber;
    public Integer reaminingItems;

    public Neighborhood neighborhood;
    public Geolocation geolocation;
    public Double radius = 0.5D;

    @Override
    public String toString() {
        return String.format(
            "SessionStateSearchingBag[what=%s]",
            what
        );
    }
}
