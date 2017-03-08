package com.timeout.chatbot.domain;

import java.io.Serializable;

public class Neighborhood implements Serializable {

    private String graffitiId;
    private String name;

    public Neighborhood(
        String graffitiId,
        String name
    ) {
        this.graffitiId = graffitiId;
        this.name = name;
    }

    public String getGraffitiId() {
        return graffitiId;
    }

    public String getName() {
        return name;
    }
}
