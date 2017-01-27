package com.timeout.chatbot.graffitti.domain.response.facets.v5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV5Node {

    private String id;
    private String name;
    private List<GraffittiFacetV5Node> children;

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

    public List<GraffittiFacetV5Node> getChildren() {
        return children;
    }

    public void setChildren(List<GraffittiFacetV5Node> children) {
        this.children = children;
    }
}
