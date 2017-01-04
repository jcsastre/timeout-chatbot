package com.timeout.chatbot.graffitti.domain.response.facets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryPrimary {

    private String id;
    private String name;
    @JsonProperty("children")
    private List<CategorySecondary> secondaryCategories;

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

    public List<CategorySecondary> getSecondaryCategories() {
        return secondaryCategories;
    }

    public void setSecondaryCategories(List<CategorySecondary> secondaryCategories) {
        this.secondaryCategories = secondaryCategories;
    }
}
