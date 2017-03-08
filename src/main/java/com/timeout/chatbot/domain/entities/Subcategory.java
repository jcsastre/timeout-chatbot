package com.timeout.chatbot.domain.entities;

import java.io.Serializable;

public class Subcategory implements Serializable {

    private final String graffittiId;
    private final String name;
    private final String conceptName;

    public Subcategory(
        String graffittiId,
        String name,
        String conceptName
    ) {
        this.graffittiId = graffittiId;
        this.name = name;
        this.conceptName = conceptName;
    }

    public String getGraffittiId() {
        return graffittiId;
    }

    public String getName() {
        return name;
    }

    public String getConceptName() {
        return conceptName;
    }
}
