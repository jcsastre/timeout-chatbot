package com.timeout.chatbot.graffitti.response.facets.v4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV4Facet {

    private String id;
    private String name;
    private List<GraffittiFacetV4FacetChild> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GraffittiFacetV4FacetChild> getChildren() {
        return children;
    }

    public void setChildren(List<GraffittiFacetV4FacetChild> children) {
        this.children = children;
    }
}
