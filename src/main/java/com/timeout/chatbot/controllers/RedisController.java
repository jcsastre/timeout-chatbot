package com.timeout.chatbot.controllers;

import com.timeout.chatbot.redis.Person;
import com.timeout.chatbot.redis.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final PersonRepo personRepo;

    @Autowired
    public RedisController(
        PersonRepo personRepo
    ) {
        this.personRepo = personRepo;
    }

    @RequestMapping(path="/create", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> create() {

        Person person = new Person();
        person.setId("1");
        person.setAge(55);
        person.setGender(Person.Gender.Female);
        person.setName("Oracle");
        personRepo.save(person);

        Person person2 = new Person();
        person2.setId("2");
        person2.setAge(60);
        person2.setGender(Person.Gender.Male);
        person2.setName("TheArchitect");
        personRepo.save(person2);

        Person person3 = new Person();
        person3.setId("3");
        person3.setAge(25);
        person3.setGender(Person.Gender.Male);
        person3.setName("TheOne");
        personRepo.save(person3);

        return ResponseEntity.ok("Ok");
    }

    @RequestMapping(path="/currently", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> currently() {

        Map<Object,Object> personMatrixMap = personRepo.findAll();
        System.out.println("Currently in the Redis Matrix");
        System.out.println(personMatrixMap);

        return ResponseEntity.ok("Ok");
    }

    @RequestMapping(path="/theone", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> theone() {

        System.out.println("Finding the One : "+personRepo.find("3"));

        return ResponseEntity.ok("Ok");
    }

//    @RequestMapping(path="/read", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> read() {
//
//        System.out.println("Finding the One : "+personRepo.find("3"));
//        Map<Object,Object> personMatrixMap = personRepo.findAll();
//        System.out.println("Currently in the Redis Matrix");
//        System.out.println(personMatrixMap);
//        System.out.println("Deleting The Architect ");
//        personRepo.delete("2");
//        personMatrixMap = personRepo.findAll();
//        System.out.println("Remnants .. : ");
//        System.out.println(personMatrixMap);
//
//        return ResponseEntity.ok("Ok");
//    }
}
