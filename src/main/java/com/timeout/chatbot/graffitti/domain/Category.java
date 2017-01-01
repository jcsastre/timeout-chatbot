package com.timeout.chatbot.graffitti.domain;

import java.util.UUID;

public class Category {

    private final UUID uuid;
    private final String name;
    private final Category parent;

    public Category(UUID uuid, String name, Category parent) {
        this.uuid = uuid;
        this.name = name;
        this.parent = parent;
    }

    public Category(UUID uuid, String name) {
        this(uuid, name, null);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }
}
