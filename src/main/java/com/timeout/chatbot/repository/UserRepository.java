package com.timeout.chatbot.repository;

import com.timeout.chatbot.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByMessengerId(String messengerId);

}
