package com.timeout.chatbot.graffitti.response.facets.v4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV4Response {

    private GraffittiFacetV4Body body;

    public GraffittiFacetV4Body getBody() {
        return body;
    }

    public void setBody(GraffittiFacetV4Body body) {
        this.body = body;
    }
}
