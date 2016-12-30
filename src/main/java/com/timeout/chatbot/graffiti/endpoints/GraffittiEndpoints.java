package com.timeout.chatbot.graffiti.endpoints;

public enum GraffittiEndpoints {
    RESTAURANTS("http://graffiti.timeout.com/v1/sites/es-barcelona/search?what=node-7083");

    private final String url;

    GraffittiEndpoints(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
