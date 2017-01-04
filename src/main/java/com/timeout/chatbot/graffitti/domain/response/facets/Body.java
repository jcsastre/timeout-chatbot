package com.timeout.chatbot.graffitti.domain.response.facets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {

    private Facets facets;

    public Facets getFacets() {
        return facets;
    }

    public void setFacets(Facets facets) {
        this.facets = facets;
    }
}
