package com.timeout.chatbot.graffitti.domain.response.categorisation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Categorisation {

    @JsonProperty("primary")
    private CategorisationPrimary categorisationPrimary;

    @JsonProperty("secondary")
    private CategorisationSecondary categorisationSecondary;

    public CategorisationPrimary getCategorisationPrimary() {
        return categorisationPrimary;
    }

    public void setCategorisationPrimary(CategorisationPrimary categorisationPrimary) {
        this.categorisationPrimary = categorisationPrimary;
    }

    public CategorisationSecondary getCategorisationSecondary() {
        return categorisationSecondary;
    }

    public void setCategorisationSecondary(CategorisationSecondary categorisationSecondary) {
        this.categorisationSecondary = categorisationSecondary;
    }
}
