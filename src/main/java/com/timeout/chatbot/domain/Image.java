package com.timeout.chatbot.domain;

import com.timeout.chatbot.graffitti.response.images.GraffittiImage;

import java.io.Serializable;

public class Image implements Serializable {

    public String id;
    public String url;
    public String title;
    public String altText;

    public static Image build(
        GraffittiImage gi
    ) {
        Image image = new Image();

        image.id = gi.getId();
        image.url = gi.getUrl();
        image.title = gi.getTitle();
        image.altText = gi.getAltText();

        return image;
    }
}
