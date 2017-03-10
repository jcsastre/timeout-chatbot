package com.timeout.chatbot.domain;

import java.io.Serializable;

public class CategoryNode implements Serializable {

    private String name;
    private Integer graffittiNodeId;

    public CategoryNode(
        String name,
        Integer graffittiNodeId
    ) {
        this.name = name;
        this.graffittiNodeId = graffittiNodeId;
    }

    public String getName() {
        return name;
    }

    public Integer getGraffittiNodeId() {
        return graffittiNodeId;
    }
}
