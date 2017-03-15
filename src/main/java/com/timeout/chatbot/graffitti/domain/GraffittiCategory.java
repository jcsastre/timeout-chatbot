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

    public final String graffittiId;
    public final String name;
    public final String namePlural;
    public final String conceptName;
    public final String subcategoriesName;
    public final String subcategoriesNamePlural;
    public List<GraffittiSubcategory> subcategories;

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

    public GraffittiSubcategory findSubcategoryByGraffittiId(
        String graffittiSubcategoryId
    ) {
        for (GraffittiSubcategory graffittiSubcategory : subcategories) {
            if (graffittiSubcategory.graffittiId.equalsIgnoreCase(graffittiSubcategoryId)) {
                return graffittiSubcategory;
            }
        }

        return null;
    }
}
