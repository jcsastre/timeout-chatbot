package com.timeout.chatbot.domain;

public class Neighborhood {

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
