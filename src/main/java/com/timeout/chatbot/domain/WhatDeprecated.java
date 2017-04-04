package com.timeout.chatbot.domain;

public enum WhatDeprecated {
    RESTAURANT("Restaurant", "RestaurantsManager"),
    BAR("Bars & Pubs", "Bars & Pubs"),
    HOTEL("Hotel", "Hotels"),
    FILM("Film", "Films");

    private String singularName;
    private String pluralName;
    WhatDeprecated(String singularName, String pluralName) {
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }
}
