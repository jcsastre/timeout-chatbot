package com.timeout.chatbot.domain.page;

import java.io.Serializable;

public class PageUid implements Serializable {

    private final String value;

    public PageUid(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof PageUid))return false;

        PageUid otherPageUid = (PageUid)other;
        return this.value.equals(otherPageUid.value);
    }
}
