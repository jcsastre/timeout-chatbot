package com.timeout.chatbot.graffitti.response.categorisation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraffittiCategorisation {

    @JsonProperty("primary")
    private GraffittiCategorisationPrimary graffittiCategorisationPrimary;

    @JsonProperty("secondary")
    private GraffittiCategorisationSecondary graffittiCategorisationSecondary;

    public GraffittiCategorisationPrimary getGraffittiCategorisationPrimary() {
        return graffittiCategorisationPrimary;
    }

    public void setGraffittiCategorisationPrimary(GraffittiCategorisationPrimary graffittiCategorisationPrimary) {
        this.graffittiCategorisationPrimary = graffittiCategorisationPrimary;
    }

    public GraffittiCategorisationSecondary getGraffittiCategorisationSecondary() {
        return graffittiCategorisationSecondary;
    }

    public void setGraffittiCategorisationSecondary(GraffittiCategorisationSecondary graffittiCategorisationSecondary) {
        this.graffittiCategorisationSecondary = graffittiCategorisationSecondary;
    }

    public String buildName() {
        StringBuilder sb = new StringBuilder();

        sb.append(getGraffittiCategorisationPrimary().getName());
        final GraffittiCategorisationSecondary graffittiCategorisationSecondary = getGraffittiCategorisationSecondary();
        if (graffittiCategorisationSecondary != null) {
            sb.append(", " + graffittiCategorisationSecondary.getName());
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
