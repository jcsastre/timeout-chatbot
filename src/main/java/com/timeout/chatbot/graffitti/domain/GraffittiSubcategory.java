package com.timeout.chatbot.graffitti.domain;

import java.io.Serializable;

public class GraffittiSubcategory implements Serializable {

    private final String graffittiId;
    private final String name;
    private final String conceptName;

    public GraffittiSubcategory(
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
