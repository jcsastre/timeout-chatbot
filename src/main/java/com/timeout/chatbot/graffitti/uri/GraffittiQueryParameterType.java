package com.timeout.chatbot.graffitti.uri;

public enum GraffittiQueryParameterType {
    LOCALE("locale"),
    WHAT("what"),
    WHEN("when"),
    WHERE("where"),
    TYPE("type"),
    PAGE_SIZE("page_size"),
    PAGE_NUMBER("page_number"),
    LATITUDE("latitude"),
    LONGITUDE("longitude"),
    RADIUS("radius"),
    SORT("sort");

    private final String value;
    GraffittiQueryParameterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
