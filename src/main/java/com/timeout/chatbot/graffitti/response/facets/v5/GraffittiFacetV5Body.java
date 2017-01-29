package com.timeout.chatbot.graffitti.response.facets.v5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV5Body {

    private GraffittiFacetV5Facets facets;

    public GraffittiFacetV5Facets getFacets() {
        return facets;
    }

    public void setFacets(GraffittiFacetV5Facets facets) {
        this.facets = facets;
    }
}
