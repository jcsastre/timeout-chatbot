package com.timeout.chatbot.graffitti.response.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timeout.chatbot.graffitti.response.search.page.Meta;

import java.util.List;

public class ImagesResponse {
    private Meta meta;

    @JsonProperty("body")
    private List<Image> images;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
