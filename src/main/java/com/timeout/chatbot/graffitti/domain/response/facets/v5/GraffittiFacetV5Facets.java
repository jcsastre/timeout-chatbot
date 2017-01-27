package com.timeout.chatbot.graffitti.domain.response.facets.v5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV5Facets {

    private GraffittiFacetV5What what;

    public GraffittiFacetV5What getWhat() {
        return what;
    }

    public void setWhat(GraffittiFacetV5What what) {
        this.what = what;
    }
}
