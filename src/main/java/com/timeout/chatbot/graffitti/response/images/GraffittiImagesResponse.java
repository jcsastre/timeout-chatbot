package com.timeout.chatbot.graffitti.response.images;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GraffittiImagesResponse {
    @JsonProperty("body")
    private List<GraffittiImage> graffittiImages;

    public List<GraffittiImage> getGraffittiImages() {
        return graffittiImages;
    }

    public void setGraffittiImages(List<GraffittiImage> graffittiImages) {
        this.graffittiImages = graffittiImages;
    }
}
