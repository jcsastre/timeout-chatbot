package com.timeout.chatbot.domain.messenger;

import com.timeout.chatbot.platforms.messenger.domain.UserProfile;

public class User implements Comparable<User> {
    private String uid;
    private UserProfile userProfile;

    public User(String uid) {
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
    public int compareTo(User user) {
        return this.uid.compareTo(user.getUid());
    }
}
