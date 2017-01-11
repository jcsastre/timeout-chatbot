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

    public String buildName() {
        StringBuilder sb = new StringBuilder();

        sb.append(getCategorisationPrimary().getName());
        final CategorisationSecondary categorisationSecondary = getCategorisationSecondary();
        if (categorisationSecondary != null) {
            sb.append(", " + categorisationSecondary.getName());
        }

        return sb.toString();
    }

    public String buildNameMax80() {
        final String name = buildName();

        if (name.length() <= 80) {
            return name;
        } else {
            return name.substring(0, 80);
        }
    }
}
