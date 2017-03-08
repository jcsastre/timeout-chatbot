package com.timeout.chatbot.domain.user;

import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "`user`")
public class User implements Serializable {

    @Id
    private UUID id;

    private String messengerId;

    @Embedded

    @Type(type = "com.marvinformatics.hibernate.json.JsonUserType")
    private SuggestionsDone suggestionsDone;

    public User() {
    }

    public User(
        UUID id,
        String messengerId
    ) {
        this.id = id;
        this.messengerId = messengerId;
//        this.suggestionsDone = new SuggestionsDone();
    }

    public String getMessengerId() {
        return messengerId;
    }

    public SuggestionsDone getSuggestionsDone() {
        return suggestionsDone;
    }

    public void setSuggestionsDone(SuggestionsDone suggestionsDone) {
        this.suggestionsDone = suggestionsDone;
    }
}
