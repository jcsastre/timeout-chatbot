package com.timeout.chatbot.graffitti.domain;

public enum GraffittiWhat {
    RESTAURANT("Restaurant", "Restaurants"),
    BAR("Bars & Pubs", "Bars & Pubs"),
    HOTEL("Hotel", "Hotels"),
    FILM("Film", "Films");

    private String singularName;
    private String pluralName;
    GraffittiWhat(String singularName, String pluralName) {
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
