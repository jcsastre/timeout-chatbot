package com.timeout.chatbot.graffitti.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.domain.response.categorisation.Categorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageItem {
    private static final Logger log = LoggerFactory.getLogger(PageItem.class);

    private Categorisation categorisation;
    private String id;
    private String name;
    private String summary;
    private String url;
    private String image_url;
    private String location;
    @JsonProperty("to_website")
    private String toWebsite;

    public Categorisation getCategorisation() {
        return categorisation;
    }

    public void setCategorisation(Categorisation categorisation) {
        this.categorisation = categorisation;
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

    public String getToWebsite() {
        return toWebsite;
    }

    public void setToWebsite(String toWebsite) {
        this.toWebsite = toWebsite;
    }

    @Override
    public String toString() {
        return
            "PageItem: {" +
                "id='" + id + "', " +
                "name='" + name + "', " +
                "summary='" + summary + "', " +
                "url='" + url + "', " +
                "image_url='" + image_url + "', " +
                "location='" + location + "', " +
                "toWebsite=" + toWebsite +
            "}";
    }
}
