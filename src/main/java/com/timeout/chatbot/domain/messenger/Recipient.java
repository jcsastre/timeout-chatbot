package com.timeout.chatbot.domain.messenger;

import com.timeout.chatbot.platforms.messenger.domain.UserProfile;

public class Recipient implements Comparable<Recipient> {
    private String uid;
    private UserProfile userProfile;

    public Recipient(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public int compareTo(Recipient recipient) {
        return this.uid.compareTo(recipient.getUid());
    }
}
