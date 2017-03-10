package com.timeout.chatbot.controllers;

import com.timeout.chatbot.domain.user.SuggestionsDone;
import com.timeout.chatbot.domain.user.User;
import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/postgresql")
public class PostgresqlController {

    private final UserRepository repository;

    @Autowired
    public PostgresqlController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> doGet() {


        final User user = new User();
        user.id = UUID.randomUUID().toString();
        user.messengerId = "23232332";
        user.suggestionsDone = new SuggestionsDone();

        repository.save(user);

        return ResponseEntity.ok("Ok");
    }
}
