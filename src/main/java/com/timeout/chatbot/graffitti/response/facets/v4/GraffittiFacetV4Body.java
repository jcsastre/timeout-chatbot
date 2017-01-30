package com.timeout.chatbot.graffitti.response.facets.v4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV4Body {

    private List<GraffittiFacetV4FacetNode> facets;

    public List<GraffittiFacetV4FacetNode> getFacets() {
        return facets;
    }

    public void setFacets(List<GraffittiFacetV4FacetNode> facets) {
        this.facets = facets;
    }
}
