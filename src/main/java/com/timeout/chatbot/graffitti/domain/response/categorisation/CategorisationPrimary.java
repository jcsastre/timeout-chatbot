package com.timeout.chatbot.graffitti.domain.response.categorisation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorisationPrimary {

    private String name;

    @JsonProperty("tree_node_id")
    private Integer treeNodeId;

    @JsonProperty("concept")
    private CategorisationConcept categorisationConcept;

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

    public CategorisationConcept getCategorisationConcept() {
        return categorisationConcept;
    }

    public void setCategorisationConcept(CategorisationConcept categorisationConcept) {
        this.categorisationConcept = categorisationConcept;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
