package com.timeout.chatbot.domain.what;

import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;

public class What {

    private GraffittiCategory graffittiCategory;
    private GraffittiType graffittiType;

    public What(
        GraffittiCategory graffittiCategory,
        GraffittiType graffittiType
    ) {
        this.graffittiCategory = graffittiCategory;
        this.graffittiType = graffittiType;
    }

    public GraffittiCategory getGraffittiCategory() {
        return graffittiCategory;
    }

    public GraffittiType getGraffittiType() {
        return graffittiType;
    }
}
