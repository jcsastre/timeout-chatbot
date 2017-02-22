package com.timeout.chatbot.domain.entities;

import java.util.List;

public enum Category {

    THINGS_TO_DO(
        "node-7091",
        "Things to do",
        "Things to do",
        "things to do (category)"
    ),
    RESTAURANT(
        "node-7083",
        "Restaurant",
        "Restaurants",
        "restaurants (category)"
    );

    private final String graffittiId;
    private final String name;
    private final String namePlural;
    private final String conceptName;
    private List<Subcategory> subcategories;

    Category(
        String graffittiId,
        String name,
        String namePlural,
        String conceptName
    ) {
        this.graffittiId = graffittiId;
        this.name = name;
        this.namePlural = namePlural;
        this.conceptName = conceptName;
    }

    public String getGraffittiId() {
        return graffittiId;
    }

    public String getName() {
        return name;
    }

    public String getNamePlural() {
        return namePlural;
    }

    public String getConceptName() {
        return conceptName;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
