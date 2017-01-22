package com.timeout.chatbot.graffitti.domain.response.films;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.domain.response.categorisation.GraffittiCategorisation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {

    private String id;
    private GraffittiCategorisation graffittiCategorisation;
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    private Trailer trailer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GraffittiCategorisation getGraffittiCategorisation() {
        return graffittiCategorisation;
    }

    public void setGraffittiCategorisation(GraffittiCategorisation graffittiCategorisation) {
        this.graffittiCategorisation = graffittiCategorisation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public class Trailer {
        private String html;

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getUrl() { return "https://www.youtube.com/embed/0f1_fbdB6RQ"; }
    }
}
