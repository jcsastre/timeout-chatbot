package com.timeout.chatbot.graffitti.response.search.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.categorisation.GraffittiCategorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageItem {
    private static final Logger log = LoggerFactory.getLogger(PageItem.class);

    private String type;
    @JsonProperty("categorisation")
    private GraffittiCategorisation graffittiCategorisation;
    private String id;
    private String name;
    private String summary;
    private String url;
    private String image_url;
    private String location;
    @JsonProperty("to_website")
    private String toWebsite;
    private Double distance;
    private String phone;
    private Image image;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getToWebsite() {
        return toWebsite;
    }

    public void setToWebsite(String toWebsite) {
        this.toWebsite = toWebsite;
    }

    public Double getDistance() {
        return distance;
    }

    public Integer getDistanceInMeters() {
        return new Double(distance * 1000).intValue();
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public class Image {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
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
