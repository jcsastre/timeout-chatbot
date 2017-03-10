package com.timeout.chatbot.domain.page;

import java.io.Serializable;

public class Page implements Serializable {

    public String id;

    @Override
    public String toString() {
        return String.format(
            "Page[id=%s]",
            id
        );
    }
}
