package com.timeout.chatbot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

//    private final PersonRepo personRepo;
//
//    @Autowired
//    public RedisController(
//        PersonRepo personRepo
//    ) {
//        this.personRepo = personRepo;
//    }
//
//    @RequestMapping(path="/create", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> create() {
//
//        Address address1 = new Address("Address1", "Address1", "11111", "Barcelona1");
//        Address address2 = new Address("Address2", "Address2", "22222", "Barcelona2");
//
//        Person person = new Person();
//        person.setId("1");
//        person.setAge(55);
//        person.setGender(Person.Gender.Female);
//        person.setName("Oracle");
//        person.setAddress(address1);
//        personRepo.save(person);
//
//        Person person2 = new Person();
//        person2.setId("2");
//        person2.setAge(60);
//        person2.setGender(Person.Gender.Male);
//        person2.setName("TheArchitect");
//        personRepo.save(person2);
//
//        Person person3 = new Person();
//        person3.setId("3");
//        person3.setAge(25);
//        person3.setGender(Person.Gender.Male);
//        person3.setName("TheOne");
//        person3.setAddress(address2);
//        personRepo.save(person3);
//
//        return ResponseEntity.ok("Ok");
//    }
//
//    @RequestMapping(path="/currently", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> currently() {
//
//        Map<Object,Object> personMatrixMap = personRepo.findAll();
//        System.out.println("Currently in the Redis Matrix");
//        System.out.println(personMatrixMap);
//
//        return ResponseEntity.ok("Ok");
//    }
//
//    @RequestMapping(path="/theone", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> theone() {
//
//        System.out.println("Finding the One : "+personRepo.perform("3"));
//
//        return ResponseEntity.ok("Ok");
//    }

//    @RequestMapping(path="/read", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> read() {
//
//        System.out.println("Finding the One : "+personRepo.perform("3"));
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
