package com.timeout.chatbot.graffitti.domain.response.facets.v5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV5What {
    private List<GraffittiFacetV5Node> children;

    public List<GraffittiFacetV5Node> getChildren() {
        return children;
    }

    public void setChildren(List<GraffittiFacetV5Node> children) {
        this.children = children;
    }
}
