package com.timeout.chatbot.controllers;

import com.timeout.chatbot.domain.User;
import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> doGet() {

        //TODO:
        repository.deleteAll();

        // save a couple of customers
        repository.save(new User("messenger", "2398493232", "Juan Carlos 3", "Sastre"));
//        repository.save(new User("messenger", "2342433", "Messeguer", "Sopar"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (User user : repository.findAll()) {
            System.out.println(user);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByPlatformId("2342433"));


        return ResponseEntity.ok("Ok");
    }
}
