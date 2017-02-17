package com.timeout.chatbot.domain.entities;

import java.util.List;

public class Category {
    private final String id;
    private final String name;
    private final String conceptName;
    private final List<Subcategory> subcategories;

    public Category(
        String id,
        String name,
        String conceptName,
        List<Subcategory> subcategories
    ) {
        this.id = id;
        this.name = name;
        this.conceptName = conceptName;
        this.subcategories = subcategories;
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

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }
}
