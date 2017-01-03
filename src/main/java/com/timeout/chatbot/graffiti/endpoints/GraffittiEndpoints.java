package com.timeout.chatbot.graffiti.endpoints;

public enum GraffittiEndpoints {
    RESTAURANTS("http://graffiti.timeout.com/v1/sites/uk-london/search?what=node-7083&page_size=9"),
    VENUE("http://graffiti.timeout.com/v1/sites/uk-london/venues/");

    private final String url;

    GraffittiEndpoints(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
