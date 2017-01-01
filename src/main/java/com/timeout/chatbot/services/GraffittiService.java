package com.timeout.chatbot.services;

import com.timeout.chatbot.graffitti.domain.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GraffittiService {

    private final List<Category> categories;

    public GraffittiService() {
        categories = new ArrayList<>();

        final Category thingsToDo = new Category(UUID.randomUUID(), "Things to do");
        categories.add(thingsToDo);

        final Category restaurants = new Category(UUID.randomUUID(), "Restaurants");
        categories.add(restaurants);
        categories.add(new Category(UUID.randomUUID(), "Tunisian", restaurants));
        categories.add(new Category(UUID.randomUUID(), "British", restaurants));

        final Category barsAndPubs = new Category(UUID.randomUUID(), "Bars and pubs");
        categories.add(barsAndPubs);

        final Category art = new Category(UUID.randomUUID(), "Art");
        categories.add(art);

        final Category theatre = new Category(UUID.randomUUID(), "Theatre");
        categories.add(theatre);

        final Category music = new Category(UUID.randomUUID(), "Music");
        categories.add(music);

        final Category nightlife = new Category(UUID.randomUUID(), "Nightlife");
        categories.add(nightlife);

        final Category film = new Category(UUID.randomUUID(), "Film");
        categories.add(film);
    }

    public List<Category> getPrimaryCategories() {
        List<Category> primaryCategories =  new ArrayList<>();

        for (Category category : categories) {
            if (category.getParent() == null) {
                primaryCategories.add(category);
            }
        }

        return primaryCategories;
    }

    public List<Category> getSecondaryCategories(Category primaryCategory) {
        List<Category> secondaryCategories =  new ArrayList<>();

        for (Category category : categories) {
            if (category.getParent().getUuid() == primaryCategory.getUuid()) {
                secondaryCategories.add(category);
            }
        }

        return secondaryCategories;
    }
}
