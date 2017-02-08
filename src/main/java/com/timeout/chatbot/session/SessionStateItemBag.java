package com.timeout.chatbot.session;

import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;

public class SessionStateItemBag {

    private GraffittiType graffittiType;
    private String itemId;
    private GraffittiVenueResponse graffittiVenueResponse;

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }

    public void setGraffittiType(GraffittiType graffittiType) {
        this.graffittiType = graffittiType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public GraffittiVenueResponse getGraffittiVenueResponse() {
        return graffittiVenueResponse;
    }

    public void setGraffittiVenueResponse(GraffittiVenueResponse graffittiVenueResponse) {
        this.graffittiVenueResponse = graffittiVenueResponse;
    }
}
