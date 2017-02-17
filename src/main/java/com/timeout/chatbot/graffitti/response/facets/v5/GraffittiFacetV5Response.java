package com.timeout.chatbot.graffitti.response.facets.v5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV5Response {

    private GraffittiFacetV5Body body;

    public GraffittiFacetV5Body getBody() {
        return body;
    }

    public void setBody(GraffittiFacetV5Body body) {
        this.body = body;
    }
}
