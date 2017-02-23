package com.timeout.chatbot.domain.entities;

import java.util.List;

public enum Category {

    RESTAURANT(
        "node-7083",
        "Restaurant",
        "Restaurants",
        "restaurants (category)",
        "Cuisine"
    ),

    THINGS_TO_DO(
        "node-7091",
        "Things to do",
        "Things to do",
        "things to do (category)",
        "Subcategory"
    );

    private final String graffittiId;
    private final String name;
    private final String namePlural;
    private final String conceptName;
    private final String subcategoriesName;
    private List<Subcategory> subcategories;

    Category(
        String graffittiId,
        String name,
        String namePlural,
        String conceptName,
        String subcategoriesName
    ) {
        this.graffittiId = graffittiId;
        this.name = name;
        this.namePlural = namePlural;
        this.conceptName = conceptName;
        this.subcategoriesName = subcategoriesName;
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

    public String getSubcategoriesName() {
        return subcategoriesName;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Subcategory findSubcategoryByGraffittiId(
        String graffittiId
    ) {
        for (Subcategory subcategory : getSubcategories()) {
            if (subcategory.getGraffittiId().equalsIgnoreCase(graffittiId)) {
                return subcategory;
            }
        }

        return null;
    }
}
