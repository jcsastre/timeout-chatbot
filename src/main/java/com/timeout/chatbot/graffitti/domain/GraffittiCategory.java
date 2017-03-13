package com.timeout.chatbot.graffitti.domain;

import java.io.Serializable;
import java.util.List;

public enum GraffittiCategory implements Serializable {

    RESTAURANTS(
        "node-7083",
        "Restaurant",
        "Restaurants",
        "restaurants (category)",
        "Cuisine",
        "Cuisines"
    ),

    HOTELS(
        "node-7099",
        "Hotel",
        "Hotels",
        "hotels (category)",
        "Type",
        "Types"
    ),

//    BARS(
//        "node-7067",
//        "Bars & Pubs",
//        "Bars & Pubs",
//        "bars and pubs (category)",
//        "Type",
//        "Types"
//    ),

    THINGS_TO_DO(
        "node-7091",
        "Things to do",
        "Things to do",
        "things to do (category)",
        "Subcategory",
        "Subcategories"
    );

//    FILMS(
//        "node-7073",
//        "Film",
//        "Films",
//        "FILM (category)",
//        "Genre",
//        "Genres"
//    );

    private final String graffittiId;
    private final String name;
    private final String namePlural;
    private final String conceptName;
    private final String subcategoriesName;
    private final String subcategoriesNamePlural;
    private List<GraffittiSubcategory> subcategories;

    GraffittiCategory(
        String graffittiId,
        String name,
        String namePlural,
        String conceptName,
        String subcategoriesName,
        String subcategoriesNamePlural
    ) {
        this.graffittiId = graffittiId;
        this.name = name;
        this.namePlural = namePlural;
        this.conceptName = conceptName;
        this.subcategoriesName = subcategoriesName;
        this.subcategoriesNamePlural = subcategoriesNamePlural;
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

    public String getSubcategoriesNamePlural() {
        return subcategoriesNamePlural;
    }

    public List<GraffittiSubcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<GraffittiSubcategory> graffittiSubcategories) {
        this.subcategories = graffittiSubcategories;
    }

    public GraffittiSubcategory findSubcategoryByGraffittiId(
        String graffittiSubcategoryId
    ) {
        for (GraffittiSubcategory graffittiSubcategory : getSubcategories()) {
            if (graffittiSubcategory.graffittiId.equalsIgnoreCase(graffittiSubcategoryId)) {
                return graffittiSubcategory;
            }
        }

        return null;
    }

//    @Override
//    public String toString() {
//        return String.format(
//            "GraffittiCategory=%s",
//            namePlural
//        );
//    }
}
