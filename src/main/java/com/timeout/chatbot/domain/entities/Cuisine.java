package com.timeout.chatbot.domain.entities;

public class Cuisine {

    private final String id;
    private final String name;

    public Cuisine(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
