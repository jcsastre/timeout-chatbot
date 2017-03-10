package com.timeout.chatbot.domain.what;

import com.timeout.chatbot.graffitti.domain.GraffittiCategory;
import com.timeout.chatbot.graffitti.domain.GraffittiType;

public class Venue extends What {

    public Venue(
        GraffittiCategory graffittiCategory
    ) {
        super(
            GraffittiType.venue,
            graffittiCategory
        );
    }
}
