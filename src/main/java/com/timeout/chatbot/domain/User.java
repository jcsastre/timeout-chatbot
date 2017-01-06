package com.timeout.chatbot.domain;

import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

public class User {
    @Id
    public String id;

    public String platform;
    public String platformId;

    public String firstName;
    public String lastName;

    public UserSuggestionsDone suggestionsDone;

    public User() {}

    public User(
        String platform,
        String platformId,
        String firstName,
        String lastName
    ) {
        this.platform = platform;
        this.platformId = platformId;
        this.firstName = firstName;
        this.lastName = lastName;

        this.suggestionsDone = new UserSuggestionsDone();
    }

    @Override
    public String toString() {
        return String.format(
            "User[id=%s, platform=%s, platformId=%s, firstName='%s', lastName='%s']",
            id, platform, platformId, firstName, lastName
        );
    }

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {

    }
}
