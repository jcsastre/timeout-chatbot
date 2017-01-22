package com.timeout.chatbot.graffitti.domain.response.venues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.domain.response.categorisation.GraffittiCategorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
    private static final Logger log = LoggerFactory.getLogger(Body.class);

    private GraffittiCategorisation graffittiCategorisation;
    private String id;
    private String name;
    private String summary;
    private String url;
    private String image_url;
    private String location;
    private String annotation;
    @JsonProperty("to_website")
    private String toWebsite;

    public GraffittiCategorisation getGraffittiCategorisation() {
        return graffittiCategorisation;
    }

    public void setGraffittiCategorisation(GraffittiCategorisation graffittiCategorisation) {
        this.graffittiCategorisation = graffittiCategorisation;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getToWebsite() {
        return toWebsite;
    }

    public void setToWebsite(String toWebsite) {
        this.toWebsite = toWebsite;
    }
}
