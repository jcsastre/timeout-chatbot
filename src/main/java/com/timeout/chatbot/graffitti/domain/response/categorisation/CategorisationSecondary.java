package com.timeout.chatbot.graffitti.domain.response.categorisation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorisationSecondary {

    private String name;

    @JsonProperty("tree_node_id")
    private Integer treeNodeId;

    private CategorisationConcept concept;

    private String colour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTreeNodeId() {
        return treeNodeId;
    }

    public void setTreeNodeId(Integer treeNodeId) {
        this.treeNodeId = treeNodeId;
    }

    public CategorisationConcept getConcept() {
        return concept;
    }

    public void setConcept(CategorisationConcept concept) {
        this.concept = concept;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
