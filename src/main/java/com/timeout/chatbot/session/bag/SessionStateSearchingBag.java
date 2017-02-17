package com.timeout.chatbot.session.bag;

import com.timeout.chatbot.graffitti.response.search.page.GraffittiSearchResponse;

public class SessionStateSearchingBag {

    private GraffittiSearchResponse graffittiSearchResponse;

    public GraffittiSearchResponse getGraffittiSearchResponse() {
        return graffittiSearchResponse;
    }

    public void setGraffittiSearchResponse(GraffittiSearchResponse graffittiSearchResponse) {
        this.graffittiSearchResponse = graffittiSearchResponse;
    }
}
