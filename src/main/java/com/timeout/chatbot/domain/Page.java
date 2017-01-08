package com.timeout.chatbot.domain;

/**
 * Page of facebook.
 */
public class Page implements Comparable<Page> {
    private String uid;

    public Page(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int compareTo(Page page) {
        return this.uid.compareTo(page.getUid());
    }

    @Override
    public String toString() {
        return String.format(
            "Page[uid=%s]",
            uid
        );
    }
}
