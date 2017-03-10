package com.timeout.chatbot.domain.user;

import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "`user`")
public class User implements Serializable {

    @Id
    public String id;

    public String messengerId;

    @Embedded
    @Type(type = "com.marvinformatics.hibernate.json.JsonUserType")
    public SuggestionsDone suggestionsDone;

    @Override
    public String toString() {
        return String.format(
            "User[id=%s, messengerId=%s, suggestionsDone=%s]",
            id, messengerId, suggestionsDone
        );
    }
}
