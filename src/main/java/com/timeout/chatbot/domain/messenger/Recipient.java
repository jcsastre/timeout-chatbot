package com.timeout.chatbot.domain.messenger;

public class Recipient implements Comparable<Recipient> {
    private String uid;

    public Recipient(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int compareTo(Recipient recipient) {
        return this.uid.compareTo(recipient.getUid());
    }
}
