package com.timeout.chatbot.domain.what;

import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;

public class What {

    private GraffittiType graffittiType;
    private GraffittiCategory graffittiCategory;

    public What(
        GraffittiType graffittiType,
        GraffittiCategory graffittiCategory
    ) {
        this.graffittiType = graffittiType;
        this.graffittiCategory = graffittiCategory;
    }

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }

    public GraffittiCategory getGraffittiCategory() {
        return graffittiCategory;
    }
}
