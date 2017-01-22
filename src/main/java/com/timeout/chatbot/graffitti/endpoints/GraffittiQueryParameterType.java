package com.timeout.chatbot.graffitti.endpoints;

public enum GraffittiQueryParameterType {
    LOCALE("locale"),
    WHAT("what"),
    TYPE("type"),
    PAGE_SIZE("pageSize"),
    PAGE_NUMBER("pageNumber"),
    LATITUDE("latitude"),
    LONGITUDE("longitude");

    private final String value;
    GraffittiQueryParameterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
