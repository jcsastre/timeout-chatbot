package com.timeout.chatbot.session;

import com.timeout.chatbot.graffitti.domain.GraffittiType;

public class SessionStateItemBag {

    private GraffittiType graffittiType;
    private String itemId;

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
}
