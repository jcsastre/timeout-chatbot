package com.timeout.chatbot.controllers;

import com.timeout.chatbot.domain.UserJpa;
import com.timeout.chatbot.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postgresql")
public class PostgresqlController {

    private final UserJpaRepository repository;

    @Autowired
    public PostgresqlController(UserJpaRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> doGet() {

        repository.save(new UserJpa("Jack", "Bauer"));
        repository.save(new UserJpa("Chloe", "O'Brian"));

        return ResponseEntity.ok("Ok");
    }
}
