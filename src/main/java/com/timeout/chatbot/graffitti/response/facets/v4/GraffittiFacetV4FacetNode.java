package com.timeout.chatbot.graffitti.response.facets.v4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiFacetV4FacetNode {

    private String id;
    private String name;
    private String key;
    private Concept concept;
    private Integer count;
    private List<GraffittiFacetV4FacetNode> children;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<GraffittiFacetV4FacetNode> getChildren() {
        return children;
    }

    public void setChildren(List<GraffittiFacetV4FacetNode> children) {
        this.children = children;
    }

    public class Concept {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
