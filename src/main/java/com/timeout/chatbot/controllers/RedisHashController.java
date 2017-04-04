package com.timeout.chatbot.controllers;

import com.timeout.chatbot.redis.PersonHash;
import com.timeout.chatbot.redis.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redish")
public class RedisHashController {

    final PersonRepository personRepository;

    @Autowired
    public RedisHashController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RequestMapping(path="/create", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> create() {

        PersonHash person = new PersonHash();
        person.setId("1");
        person.setName("Oracle");
        personRepository.save(person);

        PersonHash person2 = new PersonHash();
        person2.setId("2");
        person2.setName("TheArchitect");
        person2.setTimeToLive(2L);
        personRepository.save(person2);

        PersonHash person3 = new PersonHash();
        person3.setId("3");
        person3.setName("TheOne");
        person3.setTimeToLive(1L);
        personRepository.save(person3);

        return ResponseEntity.ok("Ok");
    }

    @RequestMapping(path="/currently", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> currently() {

        System.out.println("Currently in the Redis Matrix");

        final Iterable<PersonHash> personHashIterable = personRepository.findAll();
        personHashIterable.forEach(
            personHash -> {
                if (personHash != null) {
                    System.out.println(personHash);
                }
            }
        );

        return ResponseEntity.ok("Ok");
    }
}
