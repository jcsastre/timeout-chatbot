package com.timeout.chatbot.domain.page;

/**
 * Page of facebook.
 */
public class Page {
    private PageUid uid;

    public Page(PageUid uid) {
        this.uid = uid;
    }

    public PageUid getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof PageUid))return false;

        Page otherPage = (Page)other;
        return this.uid.equals(otherPage.uid);
    }

    @Override
    public String toString() {
        return String.format(
            "Page[uid=%s]",
            uid
        );
    }
}
