package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.domain.Venue;
import com.timeout.chatbot.graffitti.domain.GraffittiType;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;

import java.io.Serializable;

public class SessionStateItemBag implements Serializable {

    public GraffittiType graffittiType;
    public String itemId;
    public Venue venue;
    public GraffittiVenueResponse graffittiVenueResponse;
}
