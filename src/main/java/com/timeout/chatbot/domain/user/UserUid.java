package com.timeout.chatbot.domain.user;

public class UserUid {
    private final String value;

    public UserUid(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof UserUid))return false;

        UserUid otherUserUid = (UserUid)other;
        return this.value.equals(otherUserUid.value);
    }
}
