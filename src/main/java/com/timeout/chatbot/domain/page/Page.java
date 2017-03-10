package com.timeout.chatbot.domain.page;

import java.io.Serializable;

/**
 * Page of facebook.
 */
public class Page implements Serializable {

    private String id;

    public Page() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof String))return false;

        Page otherPage = (Page)other;
        return this.id.equals(otherPage.id);
    }

//    @Override
//    public String toString() {
//        return String.format(
//            "Page[id=%s]",
//            id
//        );
//    }
}
