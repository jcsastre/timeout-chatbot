package com.timeout.chatbot.graffitti.domain.response.facets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class What {
    @JsonProperty("children")
    private List<CategoryPrimary> primaryCategories;

    public List<CategoryPrimary> getPrimaryCategories() {
        return primaryCategories;
    }

    public void setPrimaryCategories(List<CategoryPrimary> primaryCategories) {
        this.primaryCategories = primaryCategories;
    }
}
