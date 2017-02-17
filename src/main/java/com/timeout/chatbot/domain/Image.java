package com.timeout.chatbot.domain;

import com.timeout.chatbot.graffitti.response.images.GraffittiImage;

public class Image {
    private String id;
    private String url;
    private String title;
    private String altText;

    public Image(GraffittiImage gi) {
        id = gi.getId();
        url = gi.getUrl();
        title = gi.getTitle();
        altText = gi.getAltText();
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getAltText() {
        return altText;
    }
}
