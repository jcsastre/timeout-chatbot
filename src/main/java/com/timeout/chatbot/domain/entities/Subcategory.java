package com.timeout.chatbot.domain.entities;

public class Subcategory {

    private final String id;
    private final String name;
    private final String conceptName;

    public Subcategory(
        String id,
        String name,
        String conceptName
    ) {
        this.id = id;
        this.name = name;
        this.conceptName = conceptName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConceptName() {
        return conceptName;
    }
}
