package com.timeout.chatbot.domain.messenger;

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
}
